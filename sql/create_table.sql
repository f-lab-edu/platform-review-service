CREATE TABLE `members` (
	`member_id`	bigint	NOT NULL primary key auto_increment,
	`username`	varchar(15)	NOT NULL,
	`password`	varchar(255)	NOT NULL,
	`role`	enum('ROLE_ADMIN', 'ROLE_USER')	NOT NULL
);

CREATE TABLE `platform` (
	`platform_id`	bigint	NOT NULL primary key auto_increment,
	`member_id`	bigint	NOT NULL,
	`name`	varchar(20)	NOT NULL,
	`url`	varchar(50)	NOT NULL,
	`description`	varchar(50)	NOT NULL,
	`created_dt`	datetime	NOT NULL,
	`modified_dt`	datetime	NOT NULL,
	`status`	enum('WAIT', 'ACCEPT', 'DENY')	NOT NULL,
	`star`	tinyint	NOT NULL,
	`version`	bigint	NOT NULL
);

CREATE TABLE `review` (
	`review_id`	bigint	NOT NULL primary key auto_increment,
	`platform_id`	bigint	NOT NULL,
	`member_id`	bigint	NOT NULL,
	`content`	varchar(500)	NOT NULL,
	`star`	tinyint	NOT NULL,
	`created_dt`	datetime	NOT NULL,
	`modified_dt`	datetime	NOT NULL
);

alter table platform add foreign key(member_id) references members(member_id);
alter table review add foreign key(platform_id) references platform(platform_id);
alter table review add foreign key(member_id) references members(member_id);