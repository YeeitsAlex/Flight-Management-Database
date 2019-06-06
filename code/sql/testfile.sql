-- SELECT *
-- FROM Pilot
-- WHERE fullname = 'name name';

----------- Test Pilot Sequence -------------------

-- SELECT *
-- FROM Pilot;
--
-- SELECT *
-- FROM Pilot
-- WHERE fullname = 'Hermano 1';
----------------------------------------------------

------------------ Test Technician Sequence -------
--INSERT INTO Technician (full_name) VALUES ('name1');

--SELECT *
--FROM Technician
--WHERE full_name = 'Hermano 1';

--------------------------------------------------------

----------- Test Pilot Sequence -------------------

-- SELECT *
-- FROM Plane;
--
-- SELECT *
-- FROM Plane
-- WHERE make = 'Hermanos';
----------------------------------------------------

----------- Check Data -------------------

-- SELECT *
-- FROM Flight;
--
-- SELECT *
-- FROM FlightInfo;

----------------------------------------------------



------------Test Book Flight-------------------------
SELECT *
FROM FLIGHT;
-- WHERE fid = 10;
--
-- SELECT *
-- FROM Reservation
-- WHERE fid = 20;
--
SELECT *
FROM Reservation
WHERE rnum > 9999;

SELECT *
FROM Reservation
WHERE cid = 1;

--
-- SELECT *
-- FROM FLIGHT
-- WHERE fnum = 1;


----------------------------------------------------
