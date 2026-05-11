-----------------------------------------------------------------------------------------------
--Students:
--Meylin Carvajal Baltodano
--Luis D. Rivas Castro
--Ma. Fernanda Vega Acosta

-----------------------------------------------------------------------------------------------

-- Health catalog seed data for the JavaFX forms.
-- These are catalog labels only; diagnosis and dose must be decided by a veterinarian.

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Kennel cough'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'kennel cough');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Canine parvovirus'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'canine parvovirus');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Feline URI'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'feline uri');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Panleukopenia'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'panleukopenia');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Flea allergy'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'flea allergy');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Pneumonia'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'pneumonia');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Parasites'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'parasites');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Wound infection'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'wound infection');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Shell rot'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'shell rot');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Mouth rot'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'mouth rot');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Metabolic bone disease'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'metabolic bone disease');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Respiratory infection'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'respiratory infection');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Aspergillosis'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'aspergillosis');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Candidiasis'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'candidiasis');

INSERT INTO disease(id, name)
SELECT sDisease.NEXTVAL, 'Mites'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM disease WHERE LOWER(name) = 'mites');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Supportive care'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'supportive care');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Fluid therapy'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'fluid therapy');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Antibiotic therapy'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'antibiotic therapy');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Antifungal therapy'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'antifungal therapy');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Antiparasitic care'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'antiparasitic care');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Flea control'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'flea control');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Wound cleaning'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'wound cleaning');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Isolation'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'isolation');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Nutrition support'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'nutrition support');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'UVB husbandry'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'uvb husbandry');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Shell care'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'shell care');

INSERT INTO treatment(id, name)
SELECT sTreatment.NEXTVAL, 'Vet monitoring'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM treatment WHERE LOWER(name) = 'vet monitoring');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Vet prescribed med'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'vet prescribed med');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Antibiotic'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'antibiotic');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Antifungal'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'antifungal');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Antiparasitic'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'antiparasitic');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Anti-inflammatory'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'anti-inflammatory');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Pain control'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'pain control');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Fluid support'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'fluid support');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Vitamin support'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'vitamin support');

INSERT INTO medicine(id, name)
SELECT sMedicine.NEXTVAL, 'Flea preventive'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicine WHERE LOWER(name) = 'flea preventive');

COMMIT;
