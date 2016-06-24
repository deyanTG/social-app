-- create admin@tillur.com with password testT123
insert into users(id, first_name, last_name, password, provider, role, username) VALUES(1, 'Cultivat', 'Admin', '$2a$10$DFZT7hs/F/U0X0ofb8Nxiudxfyys4tm4lTepRnDLxCeBCSc1b6mOa' , 'LOCAL', 'INTERNAL_ADMIN', 'admin@cultivat.com');

insert into company(id, name, description, city, country, state, street1, street2, zip, geo_location, logo, website) VALUES(1, 'Test Company 1', 'Lorem Ipsum', 'Sofia', 'Bulgaria', '', '76A James Bourchier', 'suite 2', '74567', '1,1', 'http://3.bp.blogspot.com/-gJve2HCoHFs/VbHcNFm5CII/AAAAAAAAFSY/-p2QTJzrEv0/s1600/ibm-logo-blue.gif', 'http://connectik.com');
update company set year_founded = 2015 where id = 1;
update company set phone = '123123123123' where id = 1;
update company set fax = '123123123' where id = 1;

insert into user_company VALUES(1,1,1,true);

insert into company_industry(company_id, industry, order_seq) VALUES(1, 'industry 1', 1);
insert into company_industry(company_id, industry, order_seq) VALUES(1, 'industry 2', 2);
insert into company_industry(company_id, industry, order_seq) VALUES(1, 'industry 3', 3);
insert into company_industry(company_id, industry, order_seq) VALUES(1, 'industry 4', 4);
insert into company_industry(company_id, industry, order_seq) VALUES(1, 'industry 5', 5);
insert into company_industry(company_id, industry, order_seq) VALUES(1, 'industry 6', 6);

insert into company_interest(company_id, interest, order_seq) VALUES(1, 'interest 1', 1);
insert into company_interest(company_id, interest, order_seq) VALUES(1, 'interest 2', 2);
insert into company_interest(company_id, interest, order_seq) VALUES(1, 'interest 3', 3);

insert into company_sector(company_id, sector, order_seq) VALUES(1, 'sector 1', 1);
insert into company_sector(company_id, sector, order_seq) VALUES(1, 'sector 2', 2);
insert into company_sector(company_id, sector, order_seq) VALUES(1, 'sector 3', 3);

insert into company_specialty(company_id, specialty, order_seq) VALUES(1, 'speciality 1', 1);
insert into company_specialty(company_id, specialty, order_seq) VALUES(1, 'speciality 2', 2);
insert into company_specialty(company_id, specialty, order_seq) VALUES(1, 'speciality 3', 3);

insert into opportunity(id, company_id, title, category, type, description, start_date, end_date, estimated_value) VALUES(1, 1, 'Test Opportunity', 'GOODS', 'OFFER', 'Lorem Ipsum', '2016-03-03T12:12:12Z', '2016-06-06T12:12:12Z', 100000);

insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(1, 'specialty 1', 1);
insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(1, 'specialty 2', 2);
insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(1, 'specialty 3', 3);

insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(1, 'keyword 1', 1);
insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(1, 'keyword 2', 2);
insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(1, 'keyword 3', 3);

insert into opportunity(id, company_id, title, category, type, description, start_date, end_date, estimated_value) VALUES(2, 1, 'Test Opportunity 2', 'PARTNERSHIP', 'REQUEST', 'Lorem Ipsum Bla', '2016-05-03T12:12:12Z', '2016-05-03T12:12:12Z', 10000);

insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(2, 'speciality 1', 1);
insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(2, 'speciality 2', 2);
insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(2, 'speciality 3', 3);

insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(2, 'keyword 1', 1);
insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(2, 'keyword 2', 2);
insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(2, 'keyword 3', 3);

insert into opportunity(id, company_id, title, category, type, description, start_date, end_date, estimated_value) VALUES(3, 1, 'Test Opportunity 3', 'INVESTMENT', 'OFFER', 'Lorem Ipsum Test', '2016-04-03T12:12:12Z', '2016-04-03T12:12:12Z', 99999);

insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(3, 'speciality 1', 1);
insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(3, 'speciality 2', 2);
insert into opportunity_specialty(opportunity_id, specialty, order_seq) VALUES(3, 'speciality 3', 3);

insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(3, 'keyword 1', 1);
insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(3, 'keyword 2', 2);
insert into opportunity_keyword(opportunity_id, keyword, order_seq) VALUES(3, 'keyword 3', 3);

insert into users(id, first_name, last_name, password, provider, role, username, city, state, country) VALUES(2, 'Test', 'Expert', '$2a$10$DFZT7hs/F/U0X0ofb8Nxiudxfyys4tm4lTepRnDLxCeBCSc1b6mOa' , 'LOCAL', 'INTERNAL_ADMIN', 'expert@cultivat.com', 'Sofia', 'Sofia', 'Bulgaria');

insert into user_specialty VALUES(2, 'specialty 1', 0);
insert into user_specialty VALUES(2, 'specialty 2', 1);
insert into user_specialty VALUES(2, 'specialty 3', 2);