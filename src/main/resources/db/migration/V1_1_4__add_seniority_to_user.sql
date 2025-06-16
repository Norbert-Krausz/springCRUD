ALTER TABLE users ADD COLUMN seniority VARCHAR(10);

UPDATE users
SET seniority = CASE
  WHEN yearsExperience < 3 THEN 'J'
  WHEN yearsExperience > 9 THEN 'S'
  ELSE 'M'
END;