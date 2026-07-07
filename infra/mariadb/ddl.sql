create database oauth_db;
create user 'oauth-app'@'%' identified by 'oauth-app';
create user 'oauth-app'@'localhost' identified by 'oauth-app';
create user 'oauth-app'@'172.19.%' identified by 'oauth-app';
grant all on oauth_db.* to 'oauth-app'@'localhost';
grant all on oauth_db.* to 'oauth-app'@'172.19.%';
grant all on oauth_db.* to 'oauth-app'@'%';
show grants for 'oauth-app@%';
revoke all on mydiary_db.* from 'oauth-app';