-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------


-- Demo application data for UI testing and project defense.
-- Run after 20_patch_current_schema_front_alignment.sql and 21_seed_health_catalogs.sql.
--
-- Demo credentials:
-- Admin accounts: admin@bienestar.org / Admin123, coordinador@bienestar.org / Admin123
-- User accounts: all demo users use password User123!

SET SERVEROUTPUT ON;

DECLARE
    c_user_hash CONSTANT VARCHAR2(255) := 'bc5848f227cc161eb5f68dfe98cb13110a9c843ce69e953a88107d865583d397';
    c_admin_hash CONSTANT VARCHAR2(255) := '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2';

    v_count NUMBER;
    v_id NUMBER;
    v_person_id NUMBER;
    v_pet_id NUMBER;
    v_district_id NUMBER;

    FUNCTION has_column(p_table IN VARCHAR2, p_column IN VARCHAR2) RETURN BOOLEAN IS
        v_found NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO v_found
        FROM user_tab_columns
        WHERE table_name = UPPER(p_table)
          AND column_name = UPPER(p_column);

        RETURN v_found > 0;
    END;

    FUNCTION next_id(p_sequence IN VARCHAR2) RETURN NUMBER IS
        v_next NUMBER;
    BEGIN
        EXECUTE IMMEDIATE 'SELECT ' || p_sequence || '.NEXTVAL FROM dual' INTO v_next;
        RETURN v_next;
    END;

    FUNCTION short_text(p_value IN VARCHAR2, p_length IN NUMBER) RETURN VARCHAR2 IS
    BEGIN
        RETURN SUBSTR(TRIM(p_value), 1, p_length);
    END;

    FUNCTION catalog_id(
        p_table IN VARCHAR2,
        p_column IN VARCHAR2,
        p_sequence IN VARCHAR2,
        p_value IN VARCHAR2
    ) RETURN NUMBER IS
        v_catalog_id NUMBER;
    BEGIN
        EXECUTE IMMEDIATE
            'SELECT id FROM ' || p_table || ' WHERE LOWER(' || p_column || ') = LOWER(:value) AND ROWNUM = 1'
            INTO v_catalog_id
            USING p_value;

        RETURN v_catalog_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_catalog_id := next_id(p_sequence);
            EXECUTE IMMEDIATE
                'INSERT INTO ' || p_table || '(id, ' || p_column || ') VALUES (:id, :value)'
                USING v_catalog_id, p_value;
            RETURN v_catalog_id;
    END;

    FUNCTION currency_id(p_name IN VARCHAR2, p_acronym IN VARCHAR2) RETURN NUMBER IS
        v_currency_id NUMBER;
    BEGIN
        SELECT id
        INTO v_currency_id
        FROM currency
        WHERE LOWER(acronym) = LOWER(p_acronym)
          AND ROWNUM = 1;

        RETURN v_currency_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_currency_id := next_id('sCurrency');
            INSERT INTO currency(id, name, acronym)
            VALUES(v_currency_id, SUBSTR(TRIM(p_name), 1, 25), SUBSTR(TRIM(UPPER(p_acronym)), 1, 3));
            RETURN v_currency_id;
    END;

    FUNCTION breed_id(p_type_name IN VARCHAR2, p_breed_name IN VARCHAR2) RETURN NUMBER IS
        v_type_id NUMBER;
        v_breed_id NUMBER;
    BEGIN
        v_type_id := catalog_id('petType', 'name', 'sPetType', p_type_name);

        SELECT id
        INTO v_breed_id
        FROM breed
        WHERE LOWER(name) = LOWER(p_breed_name)
          AND idPetType = v_type_id
          AND ROWNUM = 1;

        RETURN v_breed_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_breed_id := next_id('sBreed');
            INSERT INTO breed(id, name, idPetType)
            VALUES(v_breed_id, SUBSTR(TRIM(p_breed_name), 1, 25), v_type_id);
            RETURN v_breed_id;
    END;

    FUNCTION ensure_email(p_email IN VARCHAR2) RETURN NUMBER IS
        v_email_id NUMBER;
    BEGIN
        SELECT id
        INTO v_email_id
        FROM email
        WHERE LOWER(emailAddress) = LOWER(p_email)
          AND ROWNUM = 1;

        RETURN v_email_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_email_id := next_id('sEmail');
            INSERT INTO email(id, emailAddress)
            VALUES(v_email_id, SUBSTR(TRIM(LOWER(p_email)), 1, 100));
            RETURN v_email_id;
    END;

    FUNCTION ensure_phone(p_phone IN VARCHAR2) RETURN NUMBER IS
        v_phone_id NUMBER;
    BEGIN
        SELECT id
        INTO v_phone_id
        FROM phone
        WHERE phoneNumber = p_phone
          AND ROWNUM = 1;

        RETURN v_phone_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_phone_id := next_id('sPhone');
            INSERT INTO phone(id, phoneNumber)
            VALUES(v_phone_id, p_phone);
            RETURN v_phone_id;
    END;

    FUNCTION ensure_district RETURN NUMBER IS
        v_province_id NUMBER;
        v_canton_id NUMBER;
        v_local_district_id NUMBER;
    BEGIN
        SELECT MIN(id)
        INTO v_local_district_id
        FROM district;

        IF v_local_district_id IS NOT NULL THEN
            RETURN v_local_district_id;
        END IF;

        v_province_id := next_id('sProvince');
        INSERT INTO province(id, name) VALUES(v_province_id, 'San Jose');

        v_canton_id := next_id('sCanton');
        INSERT INTO canton(id, idProvince, name) VALUES(v_canton_id, v_province_id, 'Central');

        v_local_district_id := next_id('sDistrict');
        INSERT INTO district(id, idCanton, name) VALUES(v_local_district_id, v_canton_id, 'Carmen');

        RETURN v_local_district_id;
    END;

    PROCEDURE link_person_email(p_person_id IN NUMBER, p_email IN VARCHAR2) IS
        v_email_id NUMBER;
    BEGIN
        v_email_id := ensure_email(p_email);

        SELECT COUNT(*)
        INTO v_count
        FROM personxEmail
        WHERE idPerson = p_person_id
          AND idEmail = v_email_id;

        IF v_count = 0 THEN
            INSERT INTO personxEmail(idPerson, idEmail) VALUES(p_person_id, v_email_id);
        END IF;
    END;

    PROCEDURE link_person_phone(p_person_id IN NUMBER, p_phone IN VARCHAR2) IS
        v_phone_id NUMBER;
    BEGIN
        v_phone_id := ensure_phone(p_phone);

        SELECT COUNT(*)
        INTO v_count
        FROM personxPhone
        WHERE idPerson = p_person_id
          AND idPhone = v_phone_id;

        IF v_count = 0 THEN
            INSERT INTO personxPhone(idPerson, idPhone) VALUES(p_person_id, v_phone_id);
        END IF;
    END;

    FUNCTION ensure_user(
        p_first_name IN VARCHAR2,
        p_second_name IN VARCHAR2,
        p_first_last_name IN VARCHAR2,
        p_second_last_name IN VARCHAR2,
        p_identification IN VARCHAR2,
        p_email IN VARCHAR2,
        p_phone IN VARCHAR2
    ) RETURN NUMBER IS
        v_existing_person_id NUMBER;
        v_account_id NUMBER;
    BEGIN
        SELECT idPerson
        INTO v_existing_person_id
        FROM appAccount
        WHERE LOWER(loginEmail) = LOWER(p_email)
          AND accountType = 'USER'
          AND ROWNUM = 1;

        RETURN v_existing_person_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_existing_person_id := next_id('sPerson');
            INSERT INTO person(id, firstName, secondName, firstLastName, secondLastName)
            VALUES(
                v_existing_person_id,
                SUBSTR(TRIM(p_first_name), 1, 20),
                SUBSTR(TRIM(p_second_name), 1, 20),
                SUBSTR(TRIM(p_first_last_name), 1, 25),
                SUBSTR(TRIM(p_second_last_name), 1, 25)
            );

            INSERT INTO adopter(idPerson, idStarRating, note)
            VALUES(v_existing_person_id, NULL, NULL);

            link_person_email(v_existing_person_id, p_email);
            link_person_phone(v_existing_person_id, p_phone);

            v_account_id := next_id('sAppAccount');

            INSERT INTO appAccount(
                id,
                accountType,
                loginEmail,
                passwordHash,
                identificationValue,
                idPerson,
                idAssociation,
                isActive,
                createdAt
            )
            VALUES(
                v_account_id,
                'USER',
                LOWER(p_email),
                c_user_hash,
                p_identification,
                v_existing_person_id,
                NULL,
                'Y',
                SYSDATE
            );

            RETURN v_existing_person_id;
    END;

    PROCEDURE ensure_admin(p_email IN VARCHAR2, p_identification IN VARCHAR2) IS
        v_account_id NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO v_count
        FROM appAccount
        WHERE LOWER(loginEmail) = LOWER(p_email)
           OR identificationValue = p_identification;

        IF v_count = 0 THEN
            v_account_id := next_id('sAppAccount');

            INSERT INTO appAccount(
                id,
                accountType,
                loginEmail,
                passwordHash,
                identificationValue,
                idPerson,
                idAssociation,
                isActive,
                createdAt
            )
            VALUES(
                v_account_id,
                'ADMIN',
                LOWER(p_email),
                c_admin_hash,
                p_identification,
                NULL,
                NULL,
                'Y',
                SYSDATE
            );
        END IF;
    END;

    FUNCTION ensure_association(p_name IN VARCHAR2) RETURN NUMBER IS
        v_association_id NUMBER;
    BEGIN
        SELECT id
        INTO v_association_id
        FROM association
        WHERE LOWER(name) = LOWER(p_name)
          AND ROWNUM = 1;

        RETURN v_association_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_association_id := next_id('sAssociation');
            INSERT INTO association(id, name)
            VALUES(v_association_id, SUBSTR(TRIM(p_name), 1, 80));
            RETURN v_association_id;
    END;

    PROCEDURE ensure_rescuer(p_person_id IN NUMBER) IS
    BEGIN
        SELECT COUNT(*)
        INTO v_count
        FROM rescuer
        WHERE idPerson = p_person_id;

        IF v_count = 0 THEN
            INSERT INTO rescuer(idPerson) VALUES(p_person_id);
        END IF;
    END;

    PROCEDURE ensure_foster_home(
        p_person_id IN NUMBER,
        p_food_donation IN VARCHAR2,
        p_notes IN VARCHAR2,
        p_type_one IN VARCHAR2,
        p_type_two IN VARCHAR2,
        p_size_one IN VARCHAR2,
        p_size_two IN VARCHAR2
    ) IS
        v_condition_id NUMBER;
        v_food_id NUMBER;
        v_type_id NUMBER;
        v_size_id NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO v_count
        FROM fosterHome
        WHERE idPerson = p_person_id;

        IF v_count = 0 THEN
            INSERT INTO fosterHome(idPerson) VALUES(p_person_id);
        END IF;

        SELECT COUNT(*)
        INTO v_count
        FROM fosterHomexFosterCondition
        WHERE idFosterHome = p_person_id;

        IF v_count = 0 THEN
            v_food_id := catalog_id('requiresFoodDonation', 'name', 'sRequiresFoodDonation', p_food_donation);
            v_condition_id := next_id('sFosterCondition');

            INSERT INTO fosterCondition(id, idFoodDonation, notes)
            VALUES(v_condition_id, v_food_id, SUBSTR(TRIM(p_notes), 1, 200));

            INSERT INTO fosterHomexFosterCondition(idFosterHome, idFosterCondition)
            VALUES(p_person_id, v_condition_id);

            v_type_id := catalog_id('petType', 'name', 'sPetType', p_type_one);
            INSERT INTO fosterConditionxAccType(idFosterCondition, idPetType)
            VALUES(v_condition_id, v_type_id);

            v_type_id := catalog_id('petType', 'name', 'sPetType', p_type_two);
            INSERT INTO fosterConditionxAccType(idFosterCondition, idPetType)
            VALUES(v_condition_id, v_type_id);

            v_size_id := catalog_id('petSize', 'name', 'sPetSize', p_size_one);
            INSERT INTO fosterConditionxAccSize(idFosterCondition, idPetSize)
            VALUES(v_condition_id, v_size_id);

            v_size_id := catalog_id('petSize', 'name', 'sPetSize', p_size_two);
            INSERT INTO fosterConditionxAccSize(idFosterCondition, idPetSize)
            VALUES(v_condition_id, v_size_id);
        END IF;
    END;

    FUNCTION ensure_pet_contact(p_email IN VARCHAR2, p_phone IN VARCHAR2) RETURN NUMBER IS
        v_contact_id NUMBER;
        v_email_id NUMBER;
        v_phone_id NUMBER;
    BEGIN
        v_contact_id := next_id('sPetContact');
        INSERT INTO petContact(id) VALUES(v_contact_id);

        v_email_id := ensure_email(p_email);
        INSERT INTO petContactxEmail(idPetContact, idEmail) VALUES(v_contact_id, v_email_id);

        v_phone_id := ensure_phone(p_phone);
        INSERT INTO petContactxPhone(idPetContact, idPhone) VALUES(v_contact_id, v_phone_id);

        RETURN v_contact_id;
    END;

    FUNCTION ensure_pet_photo(p_path IN VARCHAR2) RETURN NUMBER IS
        v_photo_id NUMBER;
    BEGIN
        SELECT id
        INTO v_photo_id
        FROM petPhoto
        WHERE photoPath = p_path
          AND ROWNUM = 1;

        RETURN v_photo_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_photo_id := next_id('sPetPhoto');
            INSERT INTO petPhoto(id, photoPath)
            VALUES(v_photo_id, SUBSTR(TRIM(p_path), 1, 255));
            RETURN v_photo_id;
    END;

    FUNCTION person_by_email(p_email IN VARCHAR2) RETURN NUMBER IS
        v_found_person_id NUMBER;
    BEGIN
        SELECT idPerson
        INTO v_found_person_id
        FROM appAccount
        WHERE LOWER(loginEmail) = LOWER(p_email)
          AND accountType = 'USER'
          AND ROWNUM = 1;

        RETURN v_found_person_id;
    END;

    FUNCTION ensure_pet(
        p_owner_email IN VARCHAR2,
        p_name IN VARCHAR2,
        p_type IN VARCHAR2,
        p_breed IN VARCHAR2,
        p_status IN VARCHAR2,
        p_color IN VARCHAR2,
        p_size IN VARCHAR2,
        p_training IN VARCHAR2,
        p_age IN NUMBER,
        p_chip IN VARCHAR2,
        p_event_offset_days IN NUMBER,
        p_need_space IN VARCHAR2,
        p_energy_level IN VARCHAR2,
        p_description IN VARCHAR2,
        p_contact_email IN VARCHAR2,
        p_contact_phone IN VARCHAR2,
        p_photo_path IN VARCHAR2,
        p_reward_amount IN NUMBER,
        p_reward_currency IN VARCHAR2
    ) RETURN NUMBER IS
        v_existing_pet_id NUMBER;
        v_owner_id NUMBER;
        v_photo_id NUMBER;
        v_contact_id NUMBER;
        v_status_id NUMBER;
        v_reward_id NUMBER;
        v_type_id NUMBER;
        v_breed_id NUMBER;
        v_training_id NUMBER;
        v_color_id NUMBER;
        v_size_id NUMBER;
        v_currency_id NUMBER;
    BEGIN
        SELECT id
        INTO v_existing_pet_id
        FROM pet
        WHERE chip = p_chip
          AND ROWNUM = 1;

        RETURN v_existing_pet_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_owner_id := person_by_email(p_owner_email);
            ensure_rescuer(v_owner_id);

            v_photo_id := ensure_pet_photo(p_photo_path);
            v_contact_id := ensure_pet_contact(p_contact_email, p_contact_phone);
            v_status_id := catalog_id('petStatus', 'status', 'sPetStatus', p_status);
            v_type_id := catalog_id('petType', 'name', 'sPetType', p_type);
            v_breed_id := breed_id(p_type, p_breed);
            v_training_id := catalog_id('trainingEase', 'name', 'sTrainingEase', p_training);
            v_color_id := catalog_id('color', 'name', 'sColor', p_color);
            v_size_id := catalog_id('petSize', 'name', 'sPetSize', p_size);
            v_pet_id := next_id('sPet');

            INSERT INTO pet(
                id,
                idPetType,
                idBreed,
                idPetPhoto,
                idPetContact,
                idPetStatus,
                idTrainingEase,
                idLocation,
                name,
                description,
                needSpace,
                energyLevel,
                idColor,
                idSize,
                age,
                chip,
                eventDate
            )
            VALUES(
                v_pet_id,
                v_type_id,
                v_breed_id,
                v_photo_id,
                v_contact_id,
                v_status_id,
                v_training_id,
                v_district_id,
                SUBSTR(TRIM(p_name), 1, 25),
                SUBSTR(TRIM(p_description), 1, 50),
                SUBSTR(TRIM(p_need_space), 1, 20),
                SUBSTR(TRIM(p_energy_level), 1, 10),
                v_color_id,
                v_size_id,
                p_age,
                SUBSTR(TRIM(p_chip), 1, 30),
                TRUNC(SYSDATE) + p_event_offset_days
            );

            INSERT INTO rescuerxPet(idRescuer, idPet)
            VALUES(v_owner_id, v_pet_id);

            IF p_reward_amount IS NOT NULL AND p_reward_amount > 0 THEN
                v_reward_id := next_id('sReward');
                v_currency_id := currency_id(
                    CASE WHEN p_reward_currency = 'CRC' THEN 'Costa Rican Colon' ELSE 'US Dollar' END,
                    p_reward_currency
                );
                INSERT INTO reward(id, idPet, idCurrency, amount)
                VALUES(
                    v_reward_id,
                    v_pet_id,
                    v_currency_id,
                    p_reward_amount
                );
            END IF;

            RETURN v_pet_id;
    END;

    PROCEDURE ensure_pet_health(
        p_chip IN VARCHAR2,
        p_illness_state IN VARCHAR2,
        p_description IN VARCHAR2,
        p_disease IN VARCHAR2,
        p_treatment IN VARCHAR2,
        p_medicine IN VARCHAR2,
        p_dose IN VARCHAR2,
        p_veterinarian IN VARCHAR2
    ) IS
        v_health_id NUMBER;
        v_pet_health_count NUMBER;
        v_vet_id NUMBER;
        v_disease_id NUMBER;
        v_treatment_id NUMBER;
        v_medicine_id NUMBER;
    BEGIN
        SELECT id
        INTO v_pet_id
        FROM pet
        WHERE chip = p_chip
          AND ROWNUM = 1;

        SELECT COUNT(*)
        INTO v_pet_health_count
        FROM petxHealthStatus
        WHERE idPet = v_pet_id;

        IF v_pet_health_count = 0 THEN
            v_health_id := next_id('sHealthStatus');
            v_disease_id := catalog_id('disease', 'name', 'sDisease', p_disease);
            v_treatment_id := catalog_id('treatment', 'name', 'sTreatment', p_treatment);
            v_medicine_id := catalog_id('medicine', 'name', 'sMedicine', p_medicine);

            INSERT INTO healthStatus(id, illnessState, description)
            VALUES(v_health_id, SUBSTR(TRIM(p_illness_state), 1, 25), SUBSTR(TRIM(p_description), 1, 50));

            INSERT INTO petxHealthStatus(idPet, idHealthStatus)
            VALUES(v_pet_id, v_health_id);

            INSERT INTO healthStxDisease(idHealthStatus, idDisease, description)
            VALUES(
                v_health_id,
                v_disease_id,
                SUBSTR(TRIM(p_description), 1, 50)
            );

            INSERT INTO healthStxTreatment(idHealthStatus, idTreatment)
            VALUES(v_health_id, v_treatment_id);

            INSERT INTO healthStxMedicine(idHealthStatus, idMedicine, dose)
            VALUES(
                v_health_id,
                v_medicine_id,
                SUBSTR(TRIM(p_dose), 1, 20)
            );
        END IF;

        v_vet_id := catalog_id('veterinarian', 'name', 'sVeterinarian', p_veterinarian);

        SELECT COUNT(*)
        INTO v_count
        FROM petxVeterinarian
        WHERE idPet = v_pet_id
          AND idVeterinarian = v_vet_id;

        IF v_count = 0 THEN
            INSERT INTO petxVeterinarian(idPet, idVeterinarian)
            VALUES(v_pet_id, v_vet_id);
        END IF;
    END;

    PROCEDURE ensure_donation(
        p_donor_email IN VARCHAR2,
        p_association_name IN VARCHAR2,
        p_currency IN VARCHAR2,
        p_amount IN NUMBER,
        p_offset_days IN NUMBER
    ) IS
        v_donor_id NUMBER;
        v_association_id NUMBER;
        v_allocation_id NUMBER;
        v_donation_id NUMBER;
        v_currency_id NUMBER;
    BEGIN
        v_donor_id := person_by_email(p_donor_email);
        v_association_id := ensure_association(p_association_name);

        SELECT COUNT(*)
        INTO v_count
        FROM donation d
        JOIN personxDonation pd ON pd.idDonation = d.id
        JOIN associationxDonation ad ON ad.idDonation = d.id
        WHERE pd.idPerson = v_donor_id
          AND ad.idAssociation = v_association_id
          AND d.amount = p_amount;

        IF v_count = 0 THEN
            v_allocation_id := next_id('sDonationAllLocation');
            INSERT INTO donationAllLocation(id, allocatedAmount, percentage)
            VALUES(v_allocation_id, p_amount, 100);

            v_donation_id := next_id('sDonation');
            v_currency_id := currency_id(
                CASE WHEN p_currency = 'CRC' THEN 'Costa Rican Colon' ELSE 'US Dollar' END,
                p_currency
            );
            INSERT INTO donation(id, idCurrency, idDonAllLocation, donationDate, amount)
            VALUES(
                v_donation_id,
                v_currency_id,
                v_allocation_id,
                TRUNC(SYSDATE) + p_offset_days,
                p_amount
            );

            INSERT INTO personxDonation(idPerson, idDonation)
            VALUES(v_donor_id, v_donation_id);

            INSERT INTO associationxDonation(idAssociation, idDonation)
            VALUES(v_association_id, v_donation_id);
        END IF;
    END;

    PROCEDURE ensure_adoption(
        p_pet_chip IN VARCHAR2,
        p_adopter_email IN VARCHAR2,
        p_rescuer_email IN VARCHAR2,
        p_rating IN VARCHAR2,
        p_offset_days IN NUMBER
    ) IS
        v_adopter_id NUMBER;
        v_rescuer_id NUMBER;
        v_application_id NUMBER;
        v_rating_id NUMBER;
        v_star_rating_id NUMBER;
        v_adoption_id NUMBER;
        v_photo_id NUMBER;
        v_pet_name VARCHAR2(25);
        v_adopted_status_id NUMBER;
    BEGIN
        SELECT id, name
        INTO v_pet_id, v_pet_name
        FROM pet
        WHERE chip = p_pet_chip
          AND ROWNUM = 1;

        SELECT COUNT(*)
        INTO v_count
        FROM adoptionxPet
        WHERE idPet = v_pet_id;

        IF v_count = 0 THEN
            v_adopter_id := person_by_email(p_adopter_email);
            v_rescuer_id := person_by_email(p_rescuer_email);
            ensure_rescuer(v_adopter_id);
            ensure_rescuer(v_rescuer_id);

            v_application_id := next_id('sAdoptionAppication');
            INSERT INTO adoptionApplication(id, yard, exerciseTime, answers, otherPets, housingType)
            VALUES(v_application_id, 'Yes', '1h/day', 'Stable home and follow-up accepted', 'Yes', 'House');

            v_rating_id := next_id('sAdopterRating');
            INSERT INTO adopterRating(id, name, star, ratingDate, note)
            VALUES(v_rating_id, SUBSTR(TRIM(v_pet_name), 1, 25), p_rating, TRUNC(SYSDATE) + p_offset_days, 'Demo adoption');

            v_star_rating_id := next_id('sStarRating');
            INSERT INTO starRating(id, name, star, ratingDate)
            VALUES(v_star_rating_id, SUBSTR(TRIM(v_pet_name), 1, 20), p_rating, TRUNC(SYSDATE) + p_offset_days);

            v_adoption_id := next_id('sAdoption');
            INSERT INTO adoption(id, idApplication, idAdopterRating, adoptionDate, adopterNotes, followUpNotes)
            VALUES(
                v_adoption_id,
                v_application_id,
                v_rating_id,
                TRUNC(SYSDATE) + p_offset_days,
                'Responsible adopter',
                'Follow-up photo received'
            );

            INSERT INTO adoptionxPet(idAdoption, idPet)
            VALUES(v_adoption_id, v_pet_id);

            INSERT INTO adoptionxRescuer(idAdoption, idRescuer)
            VALUES(v_adoption_id, v_rescuer_id);

            INSERT INTO adoptionxRescuer(idAdoption, idRescuer)
            VALUES(v_adoption_id, v_adopter_id);

            v_photo_id := next_id('sAdoptionPhoto');
            INSERT INTO adoptionPhoto(id, photoType, photoPath)
            VALUES(v_photo_id, 'Follow-up', '/demo/adoptions/' || LOWER(v_pet_name) || '.jpg');

            INSERT INTO adoptionxAdpPhoto(idAdoption, idPhoto)
            VALUES(v_adoption_id, v_photo_id);

            UPDATE adopter
            SET idStarRating = v_star_rating_id,
                note = 'Positive adoption'
            WHERE idPerson = v_adopter_id;

            v_adopted_status_id := catalog_id('petStatus', 'status', 'sPetStatus', 'Adopted');

            UPDATE pet
            SET idPetStatus = v_adopted_status_id
            WHERE id = v_pet_id;

            DELETE FROM rescuerxPet WHERE idPet = v_pet_id;
            INSERT INTO rescuerxPet(idRescuer, idPet)
            VALUES(v_adopter_id, v_pet_id);
        END IF;
    END;

    PROCEDURE ensure_blacklist_demo(
        p_reporter_email IN VARCHAR2,
        p_reportee_email IN VARCHAR2,
        p_rating IN VARCHAR2,
        p_reason IN VARCHAR2
    ) IS
        v_reporter_id NUMBER;
        v_reportee_id NUMBER;
        v_rating_id NUMBER;
        v_blacklist_id NUMBER;
    BEGIN
        IF NOT has_column('blacklistReport', 'idReporter')
           OR NOT has_column('blacklistReport', 'idReportee')
           OR NOT has_column('blacklistReport', 'idStarRating') THEN
            RETURN;
        END IF;

        v_reporter_id := person_by_email(p_reporter_email);
        v_reportee_id := person_by_email(p_reportee_email);

        SELECT COUNT(*)
        INTO v_count
        FROM blacklistReport
        WHERE idReporter = v_reporter_id
          AND idReportee = v_reportee_id
          AND LOWER(reason) = LOWER(p_reason);

        IF v_count = 0 THEN
            v_rating_id := next_id('sStarRating');
            INSERT INTO starRating(id, name, star, ratingDate)
            VALUES(v_rating_id, 'Blacklist demo', p_rating, TRUNC(SYSDATE) - 7);

            v_blacklist_id := next_id('sBlackistReport');
            INSERT INTO blacklistReport(id, idReporter, idReportee, idStarRating, reason, active, reportDate)
            VALUES(
                v_blacklist_id,
                v_reporter_id,
                v_reportee_id,
                v_rating_id,
                SUBSTR(TRIM(p_reason), 1, 60),
                'Y',
                TRUNC(SYSDATE) - 7
            );
        END IF;
    END;
BEGIN
    v_district_id := ensure_district;

    ensure_admin('admin@bienestar.org', 'ADMIN-001');
    ensure_admin('coordinador@bienestar.org', 'ADMIN-002');

    v_person_id := ensure_user('Sofia', NULL, 'Mora', 'Rojas', 'USR-DEMO-001', 'sofia.rescate@demo.org', '88880001');
    ensure_rescuer(v_person_id);

    v_person_id := ensure_user('Mario', 'Jose', 'Campos', 'Vega', 'USR-DEMO-002', 'mario.foster@demo.org', '88880002');
    ensure_rescuer(v_person_id);
    ensure_foster_home(v_person_id, 'Optional', 'Accepts calm pets and short stays.', 'Dog', 'Cat', 'Small', 'Medium');

    v_person_id := ensure_user('Valeria', NULL, 'Solano', 'Diaz', 'USR-DEMO-003', 'valeria.adopta@demo.org', '88880003');

    v_person_id := ensure_user('Diego', NULL, 'Alpizar', 'Leon', 'USR-DEMO-004', 'diego.voluntario@demo.org', '88880004');
    ensure_rescuer(v_person_id);

    v_person_id := ensure_user('Ana', 'Maria', 'Castro', 'Ruiz', 'USR-DEMO-005', 'ana.rescate@demo.org', '88880005');
    ensure_rescuer(v_person_id);
    ensure_foster_home(v_person_id, 'Required', 'Has space for exotic and small pets.', 'Bird', 'Turtle', 'Small', 'Medium');

    v_person_id := ensure_user('Carlos', NULL, 'Vargas', 'Mena', 'USR-DEMO-006', 'carlos.adopta@demo.org', '88880006');

    v_id := ensure_association('Patitas');
    v_id := ensure_association('Rescate');
    v_id := ensure_association('VidaPet');

    v_pet_id := ensure_pet('sofia.rescate@demo.org', 'Luna', 'Dog', 'Labrador', 'Lost', 'Golden', 'Medium', 'Easy',
                           3, 'DEMO-LUNA-001', -12, 'Medium', 'Runner', 'Lost near the park',
                           'sofia.rescate@demo.org', '88880001', '/demo/pets/luna_before.jpg', 50000, 'CRC');
    ensure_pet_health('DEMO-LUNA-001', 'Good', 'No visible illness', 'Parasites', 'Vet monitoring',
                      'Vet prescribed med', 'Vet dose', 'Dr Rivera');

    v_pet_id := ensure_pet('diego.voluntario@demo.org', 'Milo', 'Cat', 'Siamese', 'Found', 'Cream', 'Small', 'Moderate',
                           2, 'DEMO-MILO-002', -5, 'Low', 'Calm', 'Found outside a school',
                           'diego.voluntario@demo.org', '88880004', '/demo/pets/milo_found.jpg', NULL, NULL);
    ensure_pet_health('DEMO-MILO-002', 'Fair', 'Needs deworming', 'Parasites', 'Antiparasitic care',
                      'Antiparasitic', 'Vet dose', 'Dr Rivera');

    v_pet_id := ensure_pet('ana.rescate@demo.org', 'Rocky', 'Dog', 'Mixed Breed', 'For Adoption', 'Brown', 'Large', 'Moderate',
                           6, 'DEMO-ROCKY-003', -90, 'High', 'Athletic', 'Recovered from abandonment',
                           'ana.rescate@demo.org', '88880005', '/demo/pets/rocky_recovery.jpg', NULL, NULL);
    ensure_pet_health('DEMO-ROCKY-003', 'Critical', 'Wound treatment needed', 'Wound infection', 'Wound cleaning',
                      'Antibiotic', 'Vet dose', 'Clinica Central');

    v_pet_id := ensure_pet('sofia.rescate@demo.org', 'Coco', 'Bird', 'Cockatiel', 'For Adoption', 'Gray', 'Small', 'Unknown',
                           2, 'DEMO-COCO-004', -70, 'Low', 'Walker', 'Needs calm indoor home',
                           'sofia.rescate@demo.org', '88880001', '/demo/pets/coco.jpg', NULL, NULL);
    ensure_pet_health('DEMO-COCO-004', 'Fair', 'Respiratory follow-up', 'Respiratory infection', 'Antibiotic therapy',
                      'Antibiotic', 'Vet dose', 'Exotic Vet');

    v_pet_id := ensure_pet('ana.rescate@demo.org', 'Nala', 'Cat', 'Shorthair', 'For Adoption', 'Orange', 'Small', 'Easy',
                           1, 'DEMO-NALA-005', -110, 'Low', 'Calm', 'Friendly kitten',
                           'ana.rescate@demo.org', '88880005', '/demo/pets/nala.jpg', NULL, NULL);
    ensure_pet_health('DEMO-NALA-005', 'Good', 'Flea allergy controlled', 'Flea allergy', 'Flea control',
                      'Flea preventive', 'Vet dose', 'Dr Rivera');

    v_pet_id := ensure_pet('diego.voluntario@demo.org', 'Max', 'Turtle', 'Red-eared Slider', 'For Adoption', 'Green', 'Small', 'Unknown',
                           5, 'DEMO-MAX-006', -130, 'Medium', 'Calm', 'Recovered shell issue',
                           'diego.voluntario@demo.org', '88880004', '/demo/pets/max_turtle.jpg', NULL, NULL);
    ensure_pet_health('DEMO-MAX-006', 'Bad', 'Shell care in progress', 'Shell rot', 'Shell care',
                      'Antibiotic', 'Vet dose', 'Exotic Vet');

    v_pet_id := ensure_pet('sofia.rescate@demo.org', 'Kira', 'Snake', 'Corn Snake', 'For Adoption', 'Orange', 'Small', 'Unknown',
                           3, 'DEMO-KIRA-007', -40, 'Low', 'Calm', 'Needs experienced home',
                           'sofia.rescate@demo.org', '88880001', '/demo/pets/kira_snake.jpg', NULL, NULL);
    ensure_pet_health('DEMO-KIRA-007', 'Critical', 'Bone support required', 'Metabolic bone disease', 'UVB husbandry',
                      'Vitamin support', 'Vet dose', 'Exotic Vet');

    v_pet_id := ensure_pet('diego.voluntario@demo.org', 'Bruno', 'Dog', 'Beagle', 'Found', 'White', 'Medium', 'Moderate',
                           4, 'DEMO-BRUNO-008', -3, 'Medium', 'Runner', 'Found with collar',
                           'diego.voluntario@demo.org', '88880004', '/demo/pets/bruno_found.jpg', NULL, NULL);
    ensure_pet_health('DEMO-BRUNO-008', 'Fair', 'Respiratory care', 'Pneumonia', 'Antibiotic therapy',
                      'Antibiotic', 'Vet dose', 'Clinica Central');

    v_pet_id := ensure_pet('ana.rescate@demo.org', 'Estrella', 'Spider', 'Chilean Rose', 'Lost', 'Brown', 'Small', 'Unknown',
                           1, 'DEMO-ESTRELLA-009', -18, 'Low', 'Calm', 'Lost terrarium pet',
                           'ana.rescate@demo.org', '88880005', '/demo/pets/estrella.jpg', 80, 'USD');
    ensure_pet_health('DEMO-ESTRELLA-009', 'Good', 'Preventive review', 'Mites', 'Antiparasitic care',
                      'Antiparasitic', 'Vet dose', 'Exotic Vet');

    v_pet_id := ensure_pet('sofia.rescate@demo.org', 'Toby', 'Dog', 'Golden Retriever', 'For Adoption', 'Golden', 'Large', 'Easy',
                           4, 'DEMO-TOBY-010', -20, 'High', 'Runner', 'Very social dog',
                           'sofia.rescate@demo.org', '88880001', '/demo/pets/toby.jpg', NULL, NULL);
    ensure_pet_health('DEMO-TOBY-010', 'Good', 'Ready for adoption', 'Parasites', 'Vet monitoring',
                      'Vet prescribed med', 'Vet dose', 'Dr Rivera');

    ensure_adoption('DEMO-NALA-005', 'valeria.adopta@demo.org', 'ana.rescate@demo.org', '5', -35);
    ensure_adoption('DEMO-MAX-006', 'carlos.adopta@demo.org', 'diego.voluntario@demo.org', '4', -20);

    ensure_donation('valeria.adopta@demo.org', 'Patitas', 'CRC', 25000, -30);
    ensure_donation('carlos.adopta@demo.org', 'Rescate', 'CRC', 15000, -15);
    ensure_donation('sofia.rescate@demo.org', 'VidaPet', 'USD', 100, -8);
    ensure_donation('diego.voluntario@demo.org', 'Patitas', 'CRC', 12000, -2);

    ensure_blacklist_demo('sofia.rescate@demo.org', 'carlos.adopta@demo.org', '2', 'Missed adoption follow-up twice');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Demo application data seeded successfully.');
    DBMS_OUTPUT.PUT_LINE('Users password: User123! | Admin password: Admin123');
END;
/
