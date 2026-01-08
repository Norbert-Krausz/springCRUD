-- after this migration the app will break :D will need to be updated, to be continued...

UPDATE users
SET seniority = CASE
  WHEN seniority = 'J' THEN 'Junior'
  WHEN seniority = 'S' THEN 'Senior'
  WHEN seniority = 'M' THEN 'Medium'
END;