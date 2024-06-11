-- 승인 회원 테이블
CREATE TABLE IF NOT EXISTS approve_member
(
    id int AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    reg_no varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

-- 회원 테이블
CREATE TABLE IF NOT EXISTS member
(
    user_id varchar(50) NOT NULL,
    password varchar(255) NOT NULL,
    `name` varchar(50) NOT NULL,
    reg_no varchar(255) NOT NULL UNIQUE,
    reg_date datetime NOT NULL,
    PRIMARY KEY(user_id)
);

