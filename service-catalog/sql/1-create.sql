DROP TABLE IF EXISTS "author";

CREATE TABLE "author" (
	"id"          BIGSERIAL NOT NULL PRIMARY KEY,
	"first_name"  TEXT      NOT NULL,
	"middle_name" TEXT,
	"last_name"   TEXT      NOT NULL,
	"birth_year"  INT       NOT NULL,
	"death_year"  INT
);