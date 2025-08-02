#!/usr/bin/env python3
import os
import uuid
import requests
import time
import subprocess
import logging
import sys
from pathlib import Path
from PIL import Image
from io import BytesIO

# Конфигурация
SERIAL_FILE = "/etc/advisio_device_id"
CONTEXT_PATH = "http://185.129.146.54:8181/advisio"
DEFAULT_IMAGE = "/tmp/default.jpg"
CURRENT_IMAGE = "/tmp/current.jpg"
IMAGE_URL_CACHE = "/tmp/last_image_url.txt"
UPDATE_INTERVAL = 60  # секунд между обновлениями
MAX_RETRIES = 3       # макс. попыток при ошибках
REQUEST_TIMEOUT = 15  # таймаут запросов

# Настройка логгирования
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('/var/log/advisio_display.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

def initialize_system():
    """Инициализация системы и проверка зависимостей"""
    logger.info("Initializing system...")
    try:
        # Проверка необходимых утилит
        subprocess.run(["which", "fbi"], check=True,
                      stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        subprocess.run(["which", "convert"], check=True,
                      stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        # Создание необходимых директорий
        os.makedirs(os.path.dirname(SERIAL_FILE), exist_ok=True)
        os.makedirs(os.path.dirname(IMAGE_URL_CACHE), exist_ok=True)

        logger.info("System initialization completed")
    except subprocess.CalledProcessError as e:
        logger.critical(f"Missing dependency: {e.stderr.decode().strip()}")
        sys.exit(1)
    except Exception as e:
        logger.critical(f"Initialization failed: {str(e)}")
        sys.exit(1)

def get_or_create_serial():
    """Генерация/получение уникального идентификатора устройства"""
    try:
        if os.path.exists(SERIAL_FILE):
            with open(SERIAL_FILE, "r") as f:
                serial = f.read().strip()
                if serial and len(serial) == 36:  # Проверка длины UUID
                    uuid.UUID(serial)  # Валидация формата
                    logger.info(f"Using existing serial: {serial}")
                    return serial

        # Генерация нового serial
        serial = str(uuid.uuid4())
        with open(SERIAL_FILE, "w") as f:
            f.write(serial)
        os.chmod(SERIAL_FILE, 0o644)
        logger.info(f"Generated new serial: {serial}")
        return serial

    except ValueError:
        logger.warning("Invalid serial format, generating new")
    except Exception as e:
        logger.error(f"Serial error: {str(e)}")

    # Fallback
    temp_serial = str(uuid.uuid4())
    logger.warning(f"Using temporary serial: {temp_serial}")
    return temp_serial

def create_default_image():
    """Создание изображения-заглушки"""
    if not os.path.exists(DEFAULT_IMAGE):
        logger.info("Creating default image...")
        try:
            subprocess.run(
                [
                    "convert", "-size", "800x600", "xc:black",
                    "-pointsize", "72", "-fill", "white",
                    "-gravity", "center", "-annotate", "0",
                    "Loading...", DEFAULT_IMAGE
                ],
                check=True,
                timeout=30,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE
            )
            logger.info("Default image created successfully")
        except Exception as e:
            logger.error(f"Failed to create default image: {str(e)}")
            sys.exit(1)

def make_http_request(url, method='get', **kwargs):
    """Безопасный HTTP-запрос с повторами и логированием"""
    last_exception = None
    for attempt in range(1, MAX_RETRIES + 1):
        try:
            logger.debug(f"Attempt {attempt}/{MAX_RETRIES}: {method.upper()} {url}")
            response = requests.request(
                method,
                url,
                timeout=REQUEST_TIMEOUT,
                **kwargs
            )
            response.raise_for_status()
            logger.debug(f"Request successful (status {response.status_code})")
            return response
        except requests.RequestException as e:
            last_exception = e
            logger.warning(f"Attempt {attempt} failed: {str(e)}")
            if attempt < MAX_RETRIES:
                time.sleep(5)

    logger.error(f"All attempts failed for {url}")
    raise last_exception

def check_registration(serial):
    """Проверка регистрации устройства"""
    try:
        logger.info(f"Checking registration for device {serial}")
        response = make_http_request(
            f"{CONTEXT_PATH}/is_register?serial={serial}"
        )
        result = response.text.strip().lower()

        if result == "false":
            logger.info("Device not registered, attempting registration")
            return register_device(serial)
        elif result == "true":
            logger.info("Device is already registered")
            return True
        else:
            logger.error(f"Unexpected response: {result}")
            return False

    except Exception as e:
        logger.error(f"Registration check failed: {str(e)}")
        return False

def register_device(serial):
    """Регистрация нового устройства"""
    try:
        logger.info(f"Registering device {serial}")
        response = make_http_request(
            f"{CONTEXT_PATH}/register?serial={serial}",
            method='post'
        )
        logger.info("Device registration successful")
        return True
    except Exception as e:
        logger.error(f"Registration failed: {str(e)}")
        return False

def download_image(url):
    """Загрузка изображения с детальным логированием"""
    try:
        logger.info(f"Starting image download from {url}")

        # 1. Получение метаданных
        response = make_http_request(url, stream=True)
        content_length = int(response.headers.get('Content-Length', 0))
        content_type = response.headers.get('Content-Type', 'unknown')

        logger.debug(f"Content-Type: {content_type}, Size: {content_length} bytes")

        if content_length < 1024:
            raise ValueError(f"Image too small ({content_length} bytes)")

        # 2. Потоковая загрузка
        buffer = BytesIO()
        for chunk in response.iter_content(chunk_size=8192):
            if chunk:  # Фильтрация keep-alive chunks
                buffer.write(chunk)

        if buffer.getbuffer().nbytes == 0:
            raise ValueError("Received empty image data")

        logger.debug(f"Downloaded {buffer.getbuffer().nbytes} bytes")

        # 3. Валидация изображения
        buffer.seek(0)
        try:
            with Image.open(buffer) as img:
                logger.debug(f"Image format: {img.format}, Mode: {img.mode}, Size: {img.size}")
                img.verify()  # Проверка целостности
        except Exception as e:
            raise ValueError(f"Image validation failed: {str(e)}")

        # 4. Конвертация и сохранение
        buffer.seek(0)
        with Image.open(buffer) as img:
            if img.mode != 'RGB':
                logger.debug(f"Converting from {img.mode} to RGB")
                img = img.convert('RGB')

            temp_path = f"{CURRENT_IMAGE}.tmp"
            img.save(temp_path, 'JPEG', quality=85, optimize=True)

            # Дополнительная проверка сохраненного файла
            with Image.open(temp_path) as saved_img:
                saved_img.verify()

            os.replace(temp_path, CURRENT_IMAGE)
            logger.info("Image successfully processed")
            return True

    except Exception as e:
        logger.error(f"Image download failed: {str(e)}")
        if 'temp_path' in locals() and os.path.exists(temp_path):
            os.remove(temp_path)
        return False

def get_image_url(serial):
    """Получение URL изображения для устройства"""
    try:
        logger.debug(f"Fetching image URL for device {serial}")
        response = make_http_request(
            f"{CONTEXT_PATH}/upload?serial={serial}"
        )
        image_url = response.text.strip()

        if not image_url.startswith(('http://', 'https://')):
            raise ValueError(f"Invalid URL format: {image_url}")

        # Проверка изменения URL
        cached_url = None
        if os.path.exists(IMAGE_URL_CACHE):
            with open(IMAGE_URL_CACHE, "r") as f:
                cached_url = f.read().strip()

        if cached_url == image_url:
            logger.info("Image URL unchanged, using cached version")
            return None  # Нет необходимости в повторной загрузке

        return image_url

    except Exception as e:
        logger.error(f"Failed to get image URL: {str(e)}")
        return None

def display_image(image_path):
    """Отображение изображения на экране"""
    try:
        if not os.path.exists(image_path):
            raise FileNotFoundError(f"Image file not found: {image_path}")

        logger.debug(f"Attempting to display {image_path}")
        result = subprocess.run(
            ["fbi", "-T", "1", "-noverbose", "-a", "-once", image_path],
            check=True,
            timeout=10,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )
        logger.info("Image displayed successfully")
        return True
    except subprocess.TimeoutExpired:
        logger.error("fbi command timed out")
    except subprocess.CalledProcessError as e:
        logger.error(f"fbi failed: {e.stderr.decode().strip()}")
    except Exception as e:
        logger.error(f"Display error: {str(e)}")
    return False

def main_loop(serial):
    """Основной рабочий цикл"""
    logger.info(f"Starting main loop for device {serial}")

    # Первоначальная проверка регистрации
    if not check_registration(serial):
        logger.warning("Using default image due to registration failure")
        if not display_image(DEFAULT_IMAGE):
            logger.critical("Failed to display default image")
            return False

    while True:
        try:
            # 1. Получение URL изображения
            image_url = get_image_url(serial)

            # 2. Загрузка нового изображения (если URL изменился)
            if image_url:
                if not download_image(image_url):
                    raise Exception("Image download failed")

                # Сохранение нового URL
                with open(IMAGE_URL_CACHE, "w") as f:
                    f.write(image_url)

            # 3. Отображение изображения
            current_image = CURRENT_IMAGE if os.path.exists(CURRENT_IMAGE) else DEFAULT_IMAGE
            if not display_image(current_image):
                raise Exception("Image display failed")

            # 4. Ожидание следующего цикла
            logger.debug(f"Waiting {UPDATE_INTERVAL} seconds before next update")
            time.sleep(UPDATE_INTERVAL)

        except KeyboardInterrupt:
            logger.info("Service stopped by user")
            break
        except Exception as e:
            logger.error(f"Main loop error: {str(e)}")
            logger.info("Attempting to display default image")
            display_image(DEFAULT_IMAGE)
            time.sleep(10)  # Задержка перед повторной попыткой

def main():
    """Точка входа в приложение"""
    try:
        logger.info("=== Starting Advisio Display Service ===")

        # Инициализация
        initialize_system()
        serial = get_or_create_serial()
        create_default_image()

        # Запуск основного цикла
        main_loop(serial)

    except Exception as e:
        logger.critical(f"Fatal error: {str(e)}", exc_info=True)
        sys.exit(1)
    finally:
        logger.info("Service stopped")

if __name__ == "__main__":
    main()