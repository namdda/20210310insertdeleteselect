select * from user;

desc myboard;

desc board;

select count(*) from board;

select * from(
			select rownum rnum, t1.* from 
				(select * from board order by group_no desc, order_no) t1) 
                
                where rnum >= 1 and rnum <= 2 ;
                
select * from board order by group_no desc, order_no;