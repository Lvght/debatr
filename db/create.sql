CREATE TABLE IF NOT EXISTS Usuario
(
    id_usuario     SERIAL UNIQUE,
    username       varchar(15)  not null unique,
    nome           VARCHAR(50)  NOT NULL,
    email          VARCHAR(255) NOT NULL UNIQUE,
    senha          VARCHAR(128) NOT NULL,
    salt           VARCHAR(128) NOT NULL,
    imagem_perfil  VARCHAR(255),
    descricao      VARCHAR(255),
    reputacao      float     DEFAULT 0.0,
    ra             INTEGER UNIQUE,
    data_criacao   TIMESTAMP DEFAULT current_timestamp,
    data_alteracao TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_Usuario PRIMARY KEY (id_usuario)
);

CREATE TABLE IF NOT EXISTS Forum
(
    id_forum        SERIAL UNIQUE,
    id_dono         INTEGER      NOT NULL,
    escopo_postagem INTEGER      NOT NULL,
    escopo_acesso   INTEGER      NOT NULL,
    titulo          VARCHAR(50)  NOT NULL,
    descricao       VARCHAR(255) NOT NULL,
    icone           VARCHAR(255) NOT NULL,
    data_criacao    TIMESTAMP DEFAULT current_timestamp,
    data_alteracao  TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_Forum PRIMARY KEY (id_forum),
    CONSTRAINT FK_User FOREIGN KEY (id_dono)
        REFERENCES Usuario (id_usuario)
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

CREATE TABLE IF NOT EXISTS Postagem
(
    id_postagem    SERIAL UNIQUE,
    id_forum       INTEGER      NOT NULL,
    id_autor       INTEGER      NOT NULL,
    id_topico      INTEGER,
    titulo         VARCHAR(255) NOT NULL,
    conteudo       TEXT         NOT NULL,
    data_criacao   TIMESTAMP DEFAULT current_timestamp,
    data_alteracao TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_Postagem PRIMARY KEY (id_postagem),
    CONSTRAINT FK_Forum FOREIGN KEY (id_forum)
        REFERENCES Forum (id_forum) ON DELETE CASCADE,
    CONSTRAINT FK_Usuario FOREIGN KEY (id_autor)
        REFERENCES Usuario (id_usuario) ON DELETE CASCADE,
    CONSTRAINT FK_Topico FOREIGN KEY (id_topico)
        REFERENCES Topico (id_topico) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Comentario
(
    id_comentario  SERIAL UNIQUE,
    id_postagem    INTEGER NOT NULL,
    id_autor       INTEGER NOT NULL,
    conteudo       TEXT    NOT NULL,
    data_criacao   TIMESTAMP DEFAULT current_timestamp,
    data_alteracao TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_Comentario PRIMARY KEY (id_comentario),
    CONSTRAINT FK_Postagem FOREIGN KEY (id_postagem)
        REFERENCES Postagem (id_postagem) ON DELETE CASCADE,
    CONSTRAINT FK_Usuario FOREIGN KEY (id_autor)
        REFERENCES Usuario (id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuario_ingressa_forum
(
    id_usuario_ingressa_forum SERIAL UNIQUE,
    id_usuario                INTEGER NOT NULL,
    id_forum                  INTEGER NOT NULL,
    data_criacao              TIMESTAMP DEFAULT current_timestamp,
    data_alteracao            TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_ingressa_forum PRIMARY KEY (id_usuario_ingressa_forum),
    CONSTRAINT FK_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario (id_usuario) ON DELETE CASCADE,
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
        REFERENCES Usuario (id_usuario) ON DELETE CASCADE,
    CONSTRAINT FK_Forum FOREIGN KEY (id_forum)
        REFERENCES Forum (id_forum) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuario_reage_postagem
(
    id_usuario_reage_postagem SERIAL UNIQUE,
    id_usuario                INTEGER NOT NULL,
    id_postagem               INTEGER NOT NULL,
    tipo_reacao               INTEGER NOT NULL,
    data_criacao              TIMESTAMP DEFAULT current_timestamp,
    data_alteracao            TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_reage_postagem PRIMARY KEY (id_usuario_reage_postagem),
    CONSTRAINT FK_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario (id_usuario) ON DELETE CASCADE,
    CONSTRAINT FK_Postagem FOREIGN KEY (id_postagem)
        REFERENCES Postagem (id_postagem) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuario_reage_comentario
(
    id_usuario_reage_comentario SERIAL UNIQUE,
    id_usuario                  INTEGER NOT NULL,
    id_comentario               INTEGER NOT NULL,
    tipo_reacao                 INTEGER NOT NULL,
    data_criacao                TIMESTAMP DEFAULT current_timestamp,
    data_alteracao              TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT PK_usuario_reage_comentario PRIMARY KEY (id_usuario_reage_comentario),
    CONSTRAINT FK_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario (id_usuario) ON DELETE CASCADE,
    CONSTRAINT FK_Comentario FOREIGN KEY (id_comentario)
        REFERENCES Comentario (id_comentario) ON DELETE CASCADE
);