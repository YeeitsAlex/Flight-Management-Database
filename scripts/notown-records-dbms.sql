DROP TABLE IF EXISTS Place, Phone, Musicians, Instrument, Songs, Album, Plays, Perform, Lives;


CREATE TABLE Place      (address varchar(300),
					    PRIMARY KEY(address)
);

CREATE TABLE Phone      (phone_no CHAR(11),
						address varchar(300) NOT NULL,
						FOREIGN KEY (address) REFERENCES Place(address)
);

CREATE TABLE Musicians  (ssn CHAR(11),
						name text,
						PRIMARY KEY(ssn)
);

CREATE TABLE Instrument (instrid Integer,
						Key text,
						dname text,
						PRIMARY KEY(instrid)
);

CREATE TABLE Album      (albumidentifier Integer,
						copyrightDate Date,
						speed float(7),
						title text,
						ssn CHAR(11) NOT NULL,
						PRIMARY KEY(albumidentifier),
						FOREIGN KEY(ssn) REFERENCES Musicians(ssn)
);


CREATE TABLE Songs      (songid bigint,
						sauthor text,
						title text,
						albumidentifier Integer NOT NULL,
						PRIMARY KEY(songid),
						FOREIGN KEY(albumidentifier) REFERENCES Album(albumidentifier)
);

CREATE TABLE Plays      (ssn CHAR(11),
						instrid Integer,
						PRIMARY KEY(ssn, instrid),
						FOREIGN KEY (ssn) REFERENCES Musicians(ssn),
						FOREIGN KEY (instrid) REFERENCES Instrument(instrid)
);

CREATE TABLE Perform    (ssn CHAR(11),
						songid bigint,
						PRIMARY KEY(ssn, songid),
						FOREIGN KEY (ssn) REFERENCES Musicians(ssn),
						FOREIGN KEY (songid) REFERENCES Songs(songid)
);


CREATE TABLE Lives      (ssn CHAR(11),
						address varchar(300),
						PRIMARY KEY(ssn, address),
						FOREIGN KEY (ssn) REFERENCES Musicians(ssn),
						FOREIGN KEY (address) REFERENCES Place(address)
);