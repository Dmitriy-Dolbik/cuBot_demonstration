SELECT ap.id FROM apartments ap
WHERE ap.id IN (SELECT us.apartment_id FROM users us
										 WHERE us.status = 'ACTIVE');


select
apartment0_.id as id1_0_0_,
users1_.chat_id as chat_id1_8_1_,
users1_.apartment_id as apartmen7_8_1_,
users1_.first_name as first_na2_8_1_,
users1_.last_name as last_nam3_8_1_,
users1_.registered_date as register4_8_1_,
users1_.status as status5_8_1_,
users1_.user_name as user_nam6_8_1_,
users1_.apartment_id as apartmen7_8_0__,
users1_.chat_id as chat_id1_8_0__
from apartments apartment0_ inner join users users1_ on apartment0_.id=users1_.apartment_id
