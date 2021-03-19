-- Create the sbd_user table, primary key sequence, update timestamp set trigger and constraints.

DROP TABLE IF EXISTS sbd_user;

DROP SEQUENCE IF EXISTS sbd_user_seq;

CREATE SEQUENCE sbd_user_seq;

CREATE TABLE sbd_user (
    sbd_user_id INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('sbd_user_seq'),
    first_name VARCHAR(255) DEFAULT NULL,
    last_name VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) NOT NULL,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TRIGGER IF EXISTS t_set_sbd_user_time_updated ON sbd_user;

CREATE TRIGGER t_set_sbd_user_time_updated
    BEFORE UPDATE
    ON sbd_user
    FOR EACH ROW
    EXECUTE PROCEDURE fn_update_timestamp();

ALTER TABLE sbd_user ADD CONSTRAINT u_sbd_user_email UNIQUE (email);
