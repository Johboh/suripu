CREATE TABLE timeline_feedback(id SERIAL PRIMARY KEY, account_id BIGINT, day TIMESTAMP, event_type INTEGER, event_datetime TIMESTAMP, created TIMESTAMP);


GRANT ALL PRIVILEGES ON timeline_feedback TO ingress_user;
GRANT ALL PRIVILEGES ON SEQUENCE timeline_feedback_id_seq TO ingress_user;


DROP TABLE timeline_feedback;

CREATE TABLE timeline_feedback(id SERIAL PRIMARY KEY, account_id BIGINT, date_of_night timestamp, old_time VARCHAR(10), new_time VARCHAR(10), event_type INTEGER, created TIMESTAMP);