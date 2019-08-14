select ip_address from log
where date between '2017-01-01.13:00:00' and '2017-01-01.14:00:00'
group by ip_address
having count(*) > 100;