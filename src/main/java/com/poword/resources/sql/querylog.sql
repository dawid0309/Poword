CREATE TABLE querylog (
                         queryid BIGINT PRIMARY KEY,  -- 使用时间戳生成时间戳的唯一ID
                         wordid INT,
                         FOREIGN KEY (wordid) REFERENCES dict(id)  -- 引用 dict 表的 id 列作为外键
);

CREATE INDEX wordid_idx ON querylog (wordid);  -- 在 wordid 列上创建索引
