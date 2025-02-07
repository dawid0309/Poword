-- dict definition

CREATE TABLE "dict" (
                      "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,
                      "word" VARCHAR(64) COLLATE NOCASE NOT NULL UNIQUE,
                      "sw" VARCHAR(64) COLLATE NOCASE NOT NULL,
                      "phonetic" VARCHAR(64),
                      "definition" TEXT,
                      "translation" TEXT,
                      "pos" VARCHAR(16),
                      "collins" INTEGER DEFAULT(0),
                      "oxford" INTEGER DEFAULT(0),
                      "tag" VARCHAR(64),
                      "bnc" INTEGER DEFAULT(NULL),
                      "frq" INTEGER DEFAULT(NULL),
                      "exchange" TEXT,
                      "detail" TEXT,
                      "audio" TEXT
);

CREATE UNIQUE INDEX "dict_idx_1" ON "dict" (id);
CREATE UNIQUE INDEX "dict_idx_2" ON "dict" (word);
CREATE INDEX "dict_idx_3" ON "dict" (sw, word COLLATE NOCASE);
CREATE INDEX "d_1" ON "dict" (word COLLATE NOCASE);

