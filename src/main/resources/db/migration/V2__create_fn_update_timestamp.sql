-- Create a function for all tables that sets the 'time_updated' column to the current timestamp on an update.

CREATE OR REPLACE FUNCTION fn_update_timestamp() RETURNS TRIGGER 
LANGUAGE plpgsql
AS
$$
    BEGIN
        NEW.time_updated = CURRENT_TIMESTAMP;
        RETURN NEW;
    END;
$$;
