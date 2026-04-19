USE `huaiwei-v2`;
ALTER TABLE system_config ADD COLUMN ai_api_key VARCHAR(255) DEFAULT NULL COMMENT 'DeepSeek R1 API Key';
