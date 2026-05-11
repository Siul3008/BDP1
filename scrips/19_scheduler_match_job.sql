
-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

--Creation of the job for matches


--JOb to make the matches between reported pets and pets that had been found

BEGIN
DBMS_SCHEDULER.create_job ( 
job_name   => 'MatchJob', 
job_type  => 'PLSQL_BLOCK',      
job_action  => 'BEGIN getMatch(NULL,NULL,NULL,NULL,NULL); END;',   
start_date  => SYSTIMESTAMP, 
repeat_interval => 'freq=HOURLY;INTERVAL=4',
end_date => NULL,       
enabled   =>TRUE,      
comments => 'Matches Job'
); 
END;
/



------------------------------------------------------------------------------------------

--An insertion so that the parameter of the frequency of the job is stored

BEGIN
    insertSysParameter('matchJobfreq', 'Frequency of the job for matches', '4');

END;
/



-------------------------------------------------------------------------------------------

--procedure to update the frequency of the job with the parameter

CREATE OR REPLACE PROCEDURE updateJobFreq 
AS
    vfreq NUMBER;
    eNoUpdate VARCHAR2(100);

BEGIN
    SELECT TO_NUMBER(value)
    INTO vfreq
    FROM sysParameter
    WHERE name = 'matchJobfreq';
    
    DBMS_SCHEDULER.DISABLE('MatchJob');
    
    DBMS_SCHEDULER.SET_ATTRIBUTE(
        name => 'MatchJob',
        attribute => 'repeat_interval',
        value => 'freq=HOURLY;INTERVAL=' || vfreq 
    );
    DBMS_SCHEDULER.ENABLE('MatchJob');

EXCEPTION
    WHEN VALUE_ERROR THEN 
    DBMS_OUTPUT.PUT_LINE('Value type or size error. Job frequency update failed');
    WHEN OTHERS THEN 
    DBMS_OUTPUT.PUT_LINE('Job frequency update. Error ' ||   SQLERRM );


END;
/
