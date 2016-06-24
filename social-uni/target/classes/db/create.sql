SET FOREIGN_KEY_CHECKS = 0;
drop table if exists users;
drop table if exists persistent_logins;
drop table if exists verification_token;
drop table if exists password_reset_token;
drop table if exists UserConnection;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  enabled bool DEFAULT TRUE,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  provider varchar(255) DEFAULT NULL,
  role varchar(255) DEFAULT 'COMPANY_USER',
  username varchar(255) DEFAULT NULL,
  avatar text,
  PRIMARY KEY (id),
  UNIQUE KEY UK_user_username (username)
);

CREATE TABLE IF NOT EXISTS persistent_logins (
    usersname VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);

CREATE TABLE IF NOT EXISTS verification_token (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20)  NOT NULL,
    token varchar(255)  NOT NULL,
    expiry_date TIMESTAMP  NOT NULL,
    used BOOL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT FK_verification_token_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION,
    CONSTRAINT token_unique_index UNIQUE (token)
);

CREATE TABLE IF NOT EXISTS password_reset_token (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20)  NOT NULL,
    token varchar(255)  NOT NULL,
    expiry_date TIMESTAMP  NOT NULL,
    used BOOL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT FK_password_reset_token_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION,
    CONSTRAINT password_reset_token_index UNIQUE (token)
);

create table  IF NOT EXISTS UserConnection (userId varchar(255) not null,
	providerId varchar(255) not null,
	providerUserId varchar(255),
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(512) not null,
	secret varchar(512),
	refreshToken varchar(512),
	expireTime bigint,
	primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
