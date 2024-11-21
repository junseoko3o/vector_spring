# CREATE TABLE IF NOT EXISTS user (
#                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
#                        email VARCHAR(255),
#                        user_name VARCHAR(255),
#                        password VARCHAR(255),
#                        login_at TIMESTAMP,
#                        created_at TIMESTAMP NOT NULL,
#                        updated_at TIMESTAMP
# );
#
# CREATE TABLE IF NOT EXISTS content (
#                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
#                          title VARCHAR(255) NOT NULL,
#                          answer TEXT NOT NULL,
#                          user_id BIGINT NOT NULL,
#                          created_at TIMESTAMP NOT NULL,
#                          updated_at TIMESTAMP,
#                          CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
# );