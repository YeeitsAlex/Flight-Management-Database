DROP TABLE IF EXISTS Professor cascade;
DROP TABLE IF EXISTS Project cascade;
DROP TABLE IF EXISTS Dept cascade;
DROP TABLE IF EXISTS Professor cascade;
DROP TABLE IF EXISTS Graduate cascade;
DROP TABLE IF EXISTS works_in;
DROP TABLE IF EXISTS work_proj;
DROP TABLE IF EXISTS work_dept;


CREATE TABLE Professor (ssn CHAR(11),
						name text,
						age Integer,
						Rank Integer,
						specialty text, 
						PRIMARY KEY(ssn));

CREATE TABlE Project   (pno Integer,
						ssn CHAR(11) NOT NULL,
						sponsor CHAR(30),
						start_Date Date,
						end_Date 	Date,
						budget REAL,
						PRIMARY KEY(pno),
						FOREIGN KEY (ssn) REFERENCES Professor(ssn) ON DELETE NO ACTION
);

CREATE TABLE works_in  (ssn CHAR(11) NOT NULL,
						pno Integer,
						PRIMARY KEY(ssn, pno),
						FOREIGN KEY (ssn) REFERENCES Professor(ssn),
						FOREIGN KEY (pno) REFERENCES Project(pno)
);


CREATE TABLE Dept      (dno Integer,
						ssn CHAR(11) NOT NULL,
						dname CHAR(40),
						office CHAR(40),
						PRIMARY KEY(dno),
						FOREIGN KEY(ssn) REFERENCES Professor(ssn) ON DELETE NO ACTION
);


CREATE TABLE Graduate  (ssn CHAR(11) NOT NULL,
						dno Integer NOT NULL,
						name CHAR(40),
						age Integer,
						deg_pg CHAR(40),
						adviser text NOT NULL,
						PRIMARY KEY(ssn),
						FOREIGN KEY (dno) REFERENCES Dept(dno) --ON DELETE NO ACTION,
						--FOREIGN KEY (advise_ssn) REFERENCES Graduate(ssn) ON DELETE NO ACTION,
);




CREATE TABLE work_proj (grad_ssn CHAR(11),
						prof_ssn CHAR(11) NOT NULL,
						pno Integer,
						since Date,
						PRIMARY KEY(grad_ssn, prof_ssn, pno),
						FOREIGN KEY (pno) REFERENCES Project(pno), --ON DELETE NO ACTION,
						FOREIGN KEY (grad_ssn) REFERENCES Graduate(ssn), --ON DELETE NO ACTION,
						FOREIGN KEY (prof_ssn) REFERENCES Professor(ssn) ON DELETE NO ACTION
);

CREATE TABLE work_dept (ssn CHAR(11),
						dno Integer NOT NULL,
						time_pc FLOAT,
						PRIMARY KEY (ssn, dno),
						FOREIGN KEY (ssn) REFERENCES Professor(ssn),
						FOREIGN KEY (dno) REFERENCES Dept(dno)
);

