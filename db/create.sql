CREATE TABLE IF NOT EXISTS debatr_user (
    id SERIAL UNIQUE PRIMARY KEY,
    username VARCHAR(15)  not null unique,
    display_name VARCHAR(50)  NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    salt VARCHAR(128) NOT NULL,
    profile_image VARCHAR(255),
    description VARCHAR(255),
    reputation FLOAT DEFAULT 0.0,
    ar INTEGER UNIQUE,
    created_at TIMESTAMP DEFAULT current_timestamp,
    updated_at TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS Forum
(
    id_forum        SERIAL UNIQUE,
    owner_id INTEGER NOT NULL REFERENCES debatr_user (id),
    escopo_postagem INTEGER      NOT NULL,
    escopo_acesso   INTEGER      NOT NULL,
    titulo          VARCHAR(50)  NOT NULL,
    descricao       VARCHAR(255) NOT NULL,
    icone           VARCHAR(255) NOT NULL,
    data_criacao    TIMESTAMP DEFAULT current_timestamp,
    data_alteracao  TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_Forum PRIMARY KEY (id_forum)
);

CREATE TABLE IF NOT EXISTS Topico
(
    id_topico      SERIAL UNIQUE,
    id_forum       INTEGER     NOT NULL,
    nome           VARCHAR(50) NOT NULL,
    data_criacao   TIMESTAMP DEFAULT current_timestamp,
    data_alteracao TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_topico PRIMARY KEY (id_topico),
    CONSTRAINT FK_Forum FOREIGN KEY (id_forum)
        REFERENCES Forum (id_forum) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS post
(
    id SERIAL UNIQUE PRIMARY KEY,
    forum  INTEGER NOT NULL REFERENCES Forum(id_forum) ON DELETE CASCADE,
    author INTEGER NOT NULL REFERENCES debatr_user (id) ON DELETE CASCADE,
    topic INTEGER REFERENCES Topico(id_topico) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp,
    updated_at TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS Comentario
(
    id_comentario  SERIAL UNIQUE,
    post_id INTEGER NOT NULL REFERENCES post(id) ON DELETE CASCADE,
    author_id INTEGER NOT NULL REFERENCES debatr_user(id) ON DELETE CASCADE,
    conteudo       TEXT    NOT NULL,
    data_criacao   TIMESTAMP DEFAULT current_timestamp,
    data_alteracao TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_Comentario PRIMARY KEY (id_comentario)
);

CREATE TABLE IF NOT EXISTS usuario_ingressa_forum
(
    id_usuario_ingressa_forum SERIAL UNIQUE,
    user_id INTEGER NOT NULL REFERENCES debatr_user (id) ON DELETE CASCADE,
    id_forum                  INTEGER NOT NULL,
    data_criacao              TIMESTAMP DEFAULT current_timestamp,
    data_alteracao            TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_ingressa_forum PRIMARY KEY (id_usuario_ingressa_forum),
    CONSTRAINT FK_Forum FOREIGN KEY (id_forum)
        REFERENCES Forum (id_forum) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuario_administra_forum
(
    id_usuario_administra_forum SERIAL UNIQUE,
    id_usuario                  INTEGER NOT NULL,
    id_forum                    INTEGER NOT NULL,
    data_criacao                TIMESTAMP DEFAULT current_timestamp,
    data_alteracao              TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_administra_forum PRIMARY KEY (id_usuario_administra_forum),
    CONSTRAINT FK_Usuario FOREIGN KEY (id_usuario)
        REFERENCES debatr_user (id) ON DELETE CASCADE,
    CONSTRAINT FK_Forum FOREIGN KEY (id_forum)
        REFERENCES Forum (id_forum) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuario_reage_postagem
(
    id_usuario_reage_postagem SERIAL UNIQUE,
    user_id INTEGER NOT NULL REFERENCES debatr_user(id) ON DELETE CASCADE,
    post_id INTEGER NOT NULL REFERENCES post(id) ON DELETE CASCADE,
    tipo_reacao               INTEGER NOT NULL,
    data_criacao              TIMESTAMP DEFAULT current_timestamp,
    data_alteracao            TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_reage_postagem PRIMARY KEY (id_usuario_reage_postagem)
);

CREATE TABLE IF NOT EXISTS usuario_reage_comentario
(
    id_usuario_reage_comentario SERIAL UNIQUE,
    user_id INTEGER NOT NULL REFERENCES debatr_user (id) ON DELETE CASCADE,
    id_comentario               INTEGER NOT NULL,
    tipo_reacao                 INTEGER NOT NULL,
    data_criacao                TIMESTAMP DEFAULT current_timestamp,
    data_alteracao              TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_reage_comentario PRIMARY KEY (id_usuario_reage_comentario),
    CONSTRAINT FK_Comentario FOREIGN KEY (id_comentario)
        REFERENCES Comentario (id_comentario) ON DELETE CASCADE
);