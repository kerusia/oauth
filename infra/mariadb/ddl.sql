create database oauth_db;
create user 'oauth-app'@'%' identified by 'oauth-app';
create user 'oauth-app'@'localhost' identified by 'oauth-app';
grant all on oauth_db.* to 'oauth-app'@'%';
grant all on oauth_db.* to 'oauth-app'@'localhost';
show grants for 'oauth-app'@'%';
show grants for 'oauth-app'@'localhost';
revoke all on oauth_db.* from 'oauth-app';