INSERT INTO "author"
("id", "first_name", "middle_name", "last_name" , "birth_year", "death_year") VALUES
(1   , 'Александр' , 'Сергеевич'  , 'Пушкин'    , 1799        , 1837        ),
(2   , 'Лев'       , 'Николаевич' , 'Толстой'   , 1828        , 1910        ),
(3   , 'Джек'      , NULL         , 'Лондон'    , 1876        , 1916        ),
(4   , 'Сергей'    , 'Васильевич' , 'Лукьяненко', 1968        , NULL        ),
(5   , 'Джоан'     , NULL         , 'Роулинг'   , 1965        , NULL        );
SELECT setval('author_id_seq', 5);