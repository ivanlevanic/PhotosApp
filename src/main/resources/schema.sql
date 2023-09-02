create table if not exists users (
    username varchar(20) not null primary key,
    password varchar(100) not null,
    email varchar(100) not null,
    firstName varchar(100) not null,
    lastName varchar(100) not null,
    enabled boolean not null
    );

create table if not exists authorities (
    id identity,
    authority varchar(20) not null,
    username varchar(20) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
    );

create table if not exists profile (
   id identity,
   username varchar(20),
    constraint fk_profile_users foreign key(username) references users(username)
    );

create table if not exists package (
    plan varchar(20) not null primary key,
    price float not null,
    uploadSize float not null,
    dailyUploadLimit int not null
    );

create table if not exists subscription (
    id identity,
    profileId long not null,
    packagePlan varchar(20) not null,
    lastRefreshDate date,
    dateOfLastChange date,
    newPackage varchar(20),
    constraint fk_subscription_profile foreign key(profileId) references profile(id),
    constraint fk_subscription_package foreign key(packagePlan) references package(plan)
    );

create table if not exists photo (
    id identity,
    profileId long not null,
    photo varchar(250) not null,
    description varchar(250),
    uploadTime datetime,
    size long not null,
    hashtags varchar(250),
    constraint fk_photo_profile foreign key(profileId) references profile(id)
    );

create table if not exists hashtag (
   id identity,
   photoId long not null,
   name varchar(30),
    constraint fk_hashtag_photo foreign key(photoId) references photo(id)
    );

create table if not exists consumption (
   id identity,
   profileId long not null,
   dayOfTheMonth date,
   numberOfUploadedPhotos int,
   constraint fk_consumption_profile foreign key(profileId) references profile(id)
    );

create table if not exists logging_system (
  id identity,
  user varchar(50) not null,
  action varchar(200) not null,
  time datetime not null
);


