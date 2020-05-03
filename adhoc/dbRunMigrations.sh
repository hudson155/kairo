# Runs database migrations
#
# For local database:
#   adhoc/dbRunMigrations.sh localhost/limber
# For local test database:
#   adhoc/dbRunMigrations.sh localhost/limber_test
# For production database:
#   adhoc/dbRunMigrations.sh "db-limber-prod-do-user-7022079-0.a.db.ondigitalocean.com:25060/limber?sslmode=require" limber $LIMBER_PROD_POSTGRES_PASSWORD

set -e

./gradlew "-PmainClass=io.limberapp.backend.adhoc.DbRunMigrationsKt" limber-backend-application:run --args="$*"
