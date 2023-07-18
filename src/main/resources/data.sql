delete from authorities;
delete from users;
delete from profile;
delete from users;
delete from subscription;


insert into users (username, password, email, firstName, lastName, enabled) values
   ('admin', '$2a$12$SXKqspTRRxX6VwNcUaWWfuDEqxc//EwXqo7VBse0bNkE0uxWmy036', 'ivanivan', 'ivan', 'levanic', 1),
   ('ivan', '$2a$12$SXKqspTRRxX6VwNcUaWWfuDEqxc//EwXqo7VBse0bNkE0uxWmy036', 'ivanivan', 'ivan', 'levanic', 1);

insert into authorities (authority, username) values
  ('ROLE_ADMIN', 'admin'),
  ('ROLE_USER', 'admin'),
  ('ROLE_USER', 'ivan');

insert into profile (id, username) values
    (1, 'admin'),
    (2, 'ivan');

insert into package (plan, price, uploadSize, dailyUploadLimit) values
    ('FREE', 0, 5.5, 3),
    ('PRO', 5, 10, 10),
    ('GOLD', 10, 15, 150);

insert into subscription (id, profileId, packagePlan) values
    (101, 2, 'FREE');

insert into consumption (id, profileId, dayOfTheMonth, numberOfUploadedPhotos) values
    (111, 2, '2022-12-07', 0);

insert into photo (id, profileId, photo, description, uploadTime, size, hashtags) values
    (1, 2,  '3bea08c6-b102-461d-852e-7e2c275a6503-SamplePicture-Koala.jpg', 'da da da', '2023-07-18 16:45:36.138016', 780892, '#ne')