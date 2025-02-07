CREATE TABLE synosModel (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         word TEXT NOT NULL,
                         synosModel JSON NOT NULL,
                         updatetime DATETIME,
                         createdtime DATETIME,
                         source TEXT,
                         FOREIGN KEY (word) REFERENCES dict(word)
);

-- 为 word 创建索引
CREATE INDEX synos_word_idx ON synos_new (word);
