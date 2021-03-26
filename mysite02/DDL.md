CREATE TABLE `board` (
   `no` int unsigned NOT NULL AUTO_INCREMENT COMMENT '번호',
   `user_no` int unsigned DEFAULT NULL COMMENT '사용자번호',
   `title` varchar(500) DEFAULT NULL COMMENT '제목',
   `group_no` int DEFAULT '1' COMMENT '묶음 번호',
   `order_no` int DEFAULT '1' COMMENT '정렬 번호',
   `depth` int DEFAULT '1' COMMENT '깊이',
   `contents` text COMMENT '내용',
   `reg_date` datetime DEFAULT NULL COMMENT '등록일',
   `cnt` int unsigned DEFAULT '0' COMMENT '조회수',
   PRIMARY KEY (`no`)
 ) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='게시판'

CREATE TABLE `guestbook` (
   `no` int unsigned NOT NULL AUTO_INCREMENT COMMENT '번호',
   `name` varchar(50) NOT NULL COMMENT '이름',
   `password` varchar(20) NOT NULL COMMENT '비밀번호',
   `contents` text NOT NULL COMMENT '내용',
   `reg_date` datetime NOT NULL COMMENT '등록일',
   PRIMARY KEY (`no`)
 ) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='방명록'

CREATE TABLE `myboard` (
   `no` int unsigned NOT NULL AUTO_INCREMENT COMMENT '번호',
   `title` varchar(100) NOT NULL COMMENT '제목',
   `pname` varchar(100) NOT NULL COMMENT '글쓴이',
   `content` varchar(5000) NOT NULL COMMENT '내용',
   `upfile` varchar(500) DEFAULT NULL COMMENT '파일',
   `regdate` datetime NOT NULL COMMENT '작성일',
   `gid` int DEFAULT NULL COMMENT '속한 번호(gid)',
   `seq` int DEFAULT NULL COMMENT '답글(seq)',
   `lev` int DEFAULT NULL COMMENT '정렬번호(lev)',
   `cnt` int unsigned DEFAULT '0' COMMENT '조회수',
   PRIMARY KEY (`no`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='내 게시판'

CREATE TABLE `user` (
   `no` int unsigned NOT NULL AUTO_INCREMENT COMMENT '번호',
   `name` varchar(50) NOT NULL COMMENT '이름',
   `email` varchar(50) NOT NULL COMMENT '이메일',
   `password` varchar(20) NOT NULL COMMENT '비밀번호',
   `gender` enum('male','female') DEFAULT NULL COMMENT '성별',
   `join_date` datetime DEFAULT NULL COMMENT '가입일',
   PRIMARY KEY (`no`)
 ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원'

CREATE TABLE `emaillist` (
   `no` int unsigned NOT NULL AUTO_INCREMENT COMMENT '번호',
   `first_name` varchar(20) NOT NULL COMMENT '성',
   `last_name` varchar(50) NOT NULL COMMENT '이름',
   `email` varchar(200) NOT NULL COMMENT '이메일',
   PRIMARY KEY (`no`)
 ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='이메일리스트'





