#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
        CREATE USER messageboard WITH PASSWORD 'password';
        CREATE DATABASE messageboard;
        GRANT ALL PRIVILEGES ON DATABASE messageboard TO messageboard
EOSQL
