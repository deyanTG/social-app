CREATE TABLE IF NOT EXISTS users
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    usersname varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    first_name varchar(255) DEFAULT NULL,
    last_name varchar(255) DEFAULT NULL,
    enabled bool DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    ROLE varchar(255) DEFAULT NULL,
    user_id bigint(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_user_role_user_id (ROLE, user_id),
    CONSTRAINT FK_user_role_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS chamber
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS company
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    chamber_id bigint(20) NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by BIGINT(20),
    updated_by BIGINT(20),
    PRIMARY KEY (id),
    CONSTRAINT FK_company_chamber FOREIGN KEY (chamber_id) REFERENCES chamber (id),
    CONSTRAINT FK_company_users_created FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT FK_company_users_updated FOREIGN KEY (updated_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_company
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) DEFAULT NULL,
    company_id bigint(20) DEFAULT NULL,
    admin Bool DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT FK_usercompanies_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_usercompanies_company FOREIGN KEY (company_id) REFERENCES company (id)
);
CREATE TABLE persistent_logins (
    usersname VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);

CREATE TABLE IF NOT EXISTS line_of_business
(
    company_id bigint(20) NOT NULL,
    name varchar(255) DEFAULT NULL,
    CONSTRAINT FK_line_of_business_company FOREIGN KEY (company_id) REFERENCES company (id)
);

CREATE TABLE IF NOT EXISTS industry
(
    company_id bigint(20) NOT NULL,
    name varchar(255) DEFAULT NULL,
    CONSTRAINT FK_industry_company FOREIGN KEY (company_id) REFERENCES company (id)
);

CREATE TABLE verification_token (
	 id bigint(20) NOT NULL AUTO_INCREMENT,
	 user_id bigint(20)  NOT NULL,
	 token varchar(255)  NOT NULL,
	 expiry_date TIMESTAMP  NOT NULL,
	 verified BOOL DEFAULT FALSE,
	 PRIMARY KEY (id),
	 CONSTRAINT FK_verification_token_users FOREIGN KEY (user_id) REFERENCES users (id),
	 CONSTRAINT token_unique_inde UNIQUE (token)
);

CREATE TABLE IF NOT EXISTS audit_record
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    request_type varchar(255) DEFAULT NULL,
    request_param varchar(255) DEFAULT NULL,
    user_id bigint(20) NULL,
    session_id varchar(255) DEFAULT NULL,
    ip_address varchar(255) DEFAULT NULL,
    user_agent varchar(255) DEFAULT NULL,
    created_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT FK_audit_user FOREIGN KEY (user_id) REFERENCES users (id)
);


