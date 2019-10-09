CREATE TABLE
    movetask
    (
        ckey INT NOT NULL AUTO_INCREMENT,
        hospital VARCHAR(32),
        department_mode VARCHAR(32),
        department_src VARCHAR(32),
        patient_name VARCHAR(64),
        patient_gender VARCHAR(10),
        patient_birthday VARCHAR(20),
        patient_age VARCHAR(20),
        patient_id VARCHAR(32),
        study_date VARCHAR(32),
        study_time VARCHAR(32),
        study_modality VARCHAR(32),
        procedure_step VARCHAR(64),
        accession_number VARCHAR(64),
        inpatient_number VARCHAR(20),
        outpatient_number VARCHAR(32),
        study_instance_uid VARCHAR(64),
        study_source VARCHAR(20),
        status VARCHAR(20) DEFAULT 'INIT',
        error_message text,
        complete INT,
        remain INT,
        failed INT,
        startMoveTime DATETIME,
        SOPInstanceUID VARCHAR(64),
        SeriesInstanceUID VARCHAR(64),
        pay_status VARCHAR(8) DEFAULT 'N',
        last_update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        qrcode VARCHAR(64),
        report_id VARCHAR(64),
        exam_status VARCHAR(16),
        report_path VARCHAR(200),
        PRIMARY KEY (ckey),
        INDEX movetask_ix2 (patient_id),
        INDEX movetask_ix3 (patient_name),
        INDEX movetask_ix4 (status),
        INDEX movetask_ix7 (accession_number),
        INDEX movetask_ix8 (study_date),
        INDEX movetask_ix9 (complete),
        INDEX movetask_ix10 (remain),
        INDEX movetask_ix11 (failed),
        INDEX movetask_ix12 (study_instance_uid),
        INDEX movetask_ix13 (SOPInstanceUID),
        INDEX movetask_ix14 (SeriesInstanceUID),
        INDEX movetask_ix15 (last_update_time)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;