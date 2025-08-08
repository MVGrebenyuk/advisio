package ru.advisio.core.services;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.advisio.core.exceptions.AdvisioCompanyGroupsException;
import ru.advisio.core.exceptions.base.AdvisioBaseException;

import java.util.Arrays;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyGroupService {

    private static final String[] DEFAULT_GROUP_NAMES = {"admin", "manager", "observer"};
    private static final String LOCATION = "location";
    private static final String REGEX = "/";
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createMainGroup(String userId, String groupName) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(groupName);

        var response = getGroupsResource().add(group);
        if (response.getStatus() == 201) {
            log.info("Created group {}", groupName);
            createSubgroups(userId, groupName);
            return groupName;
        }

        throw new RuntimeException("Failed to create group: " + response.getStatusInfo());
    }

    public void createSubgroups(String userId, String groupName) {
        Thread.ofVirtual().start(() -> {
            var group = getGroupsResource().groups()
                    .stream()
                    .filter(g -> groupName.equals(g.getName()))
                    .findFirst()
                    .orElseThrow(() -> new AdvisioCompanyGroupsException(groupName));

            GroupResource sender = getGroupsResource().group(group.getId());

            Stream.of(DEFAULT_GROUP_NAMES)
                    .parallel()
                    .map(CompanyGroupService::getGroupRepresentation)
                    .forEach(gr -> createFirstSubgroup(userId, groupName, gr, sender));
        });
    }

    private static GroupRepresentation getGroupRepresentation(String name) {
        var ret = new GroupRepresentation();
        ret.setName(name);
        return ret;
    }

    private void createFirstSubgroup(String userId, String groupName, GroupRepresentation gr, GroupResource sender) {
        try {
            var resp = sender.subGroup(gr);
            if (resp.getStatus() != 201){
                log.error("During creating subgroup for {} response {}", groupName, resp.getStatusInfo());
                throw new AdvisioCompanyGroupsException(groupName, gr.getName());
            }
            log.info("Subgrgoup {} for {} was created", gr.getName(), groupName);

            if(gr.getName().equals(DEFAULT_GROUP_NAMES[0])){
                addUserToGroup(resp, userId);
            }

        } catch (Exception e){
            log.error(e.getMessage());
            throw new AdvisioCompanyGroupsException(groupName, gr.getName());
        }
    }

    private void addUserToGroup(Response resp, String userId) {
        try {
            var subGroupId = Arrays.stream(resp.getHeaders().get(LOCATION).get(0).toString().split(REGEX)).toList().getLast();
//            var userId = getKcUser(username);
            keycloak.realm(realm).users().get(userId).joinGroup(subGroupId);
            log.info("User {} successfully added to subgroup admin");
        } catch (Exception e){
            throw new AdvisioBaseException(String.format("Failed to add user %s to subgroup admin", userId));
        }
    }

    private GroupsResource getGroupsResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.groups();
    }

    private UsersResource getUserResource(){
        RealmResource realmResource = keycloak.realm(realm);
        return  realmResource.users();
    }

    private String getKcUser(String username){
        var res = getUserResource().searchByUsername(username, true)
                .getFirst().getId();
        if(res == null){
            throw new RuntimeException("User not found in Kc");
        }

        return res;
    }

}
