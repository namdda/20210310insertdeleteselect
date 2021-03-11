desc guestbook;

-- insert
insert
  into guestbook
 values (null, '안대혁', '1234', '테스트입니다.', now());

-- select
  select no, name, date_format(reg_date, '%Y-%m-%d %H:%i:%s'), contents, password
    from guestbook
order by reg_date desc;

-- delete
delete from guestbook where no = 8 and password='1234';
  
 
 