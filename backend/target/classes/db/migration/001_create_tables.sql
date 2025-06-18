CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    year_month VARCHAR(7) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE room_players (
    id SERIAL PRIMARY KEY,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    player_id INTEGER NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(room_id, player_id)
);

CREATE TABLE rule_settings (
    id SERIAL PRIMARY KEY,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    uma_1st INTEGER DEFAULT 20000,
    uma_2nd INTEGER DEFAULT 10000,
    uma_3rd INTEGER DEFAULT -10000,
    uma_4th INTEGER DEFAULT -20000,
    oka INTEGER DEFAULT 25000,
    tobi_penalty INTEGER DEFAULT -30000,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(room_id)
);

CREATE TABLE games (
    id SERIAL PRIMARY KEY,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    game_date TIMESTAMP NOT NULL,
    starting_player_id INTEGER NOT NULL REFERENCES players(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE game_results (
    id SERIAL PRIMARY KEY,
    game_id INTEGER NOT NULL REFERENCES games(id) ON DELETE CASCADE,
    player_id INTEGER NOT NULL REFERENCES players(id),
    raw_score INTEGER NOT NULL,
    rank INTEGER NOT NULL CHECK (rank >= 1 AND rank <= 4),
    is_tobi BOOLEAN DEFAULT FALSE,
    final_score INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(game_id, player_id),
    UNIQUE(game_id, rank)
);

CREATE INDEX idx_rooms_year_month ON rooms(year_month);
CREATE INDEX idx_games_room_id ON games(room_id);
CREATE INDEX idx_games_game_date ON games(game_date);
CREATE INDEX idx_game_results_game_id ON game_results(game_id);
CREATE INDEX idx_game_results_player_id ON game_results(player_id);
