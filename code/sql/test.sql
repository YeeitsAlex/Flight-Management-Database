SELECT *
FROM Plane
WHERE make = 'tyson';
-- psql -h localhost -p $PGPORT rmace001_DB < test.sql


SELECT foo.seats - foo.num_sold FROM (
SELECT P.seats, F.num_sold
FROM Plane P, Flight F, FlightInfo FI
WHERE F.fnum = 1 AND FI.flight_id = F.fnum AND P.id = FI.plane_id) as foo;


SELECT R.plane_id, COUNT(R.rid)
FROM Repairs R
GROUP BY R.plane_id
ORDER BY COUNT(R.rid) DESC;

--LIST total number of repairs per year
SELECT EXTRACT(YEAR FROM R.repair_date), COUNT(R.rid)
FROM Repairs R
GROUP BY EXTRACT(YEAR FROM R.repair_date)
ORDER BY COUNT(R.rid) ASC;

SELECT * FROM Reservation R WHERE R.fid = 1;

SELECT * FROM Reservation R, Flight F WHERE F.fnum = 1 AND R.status = 'C' AND R.fid = F.fnum;