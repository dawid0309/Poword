-- userwordinfo definition

CREATE TABLE userwordinfo (
                            wordid INTEGER PRIMARY KEY,
                            highlighted BOOLEAN DEFAULT 0, -- 是否被划线
                            favorited BOOLEAN DEFAULT 0,   -- 是否被收藏
                            annotation TEXT,               -- 用户批注
                            updatetime DATETIME,
                            createdtime DATETIME
);
