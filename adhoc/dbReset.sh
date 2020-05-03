# Resets the database
#   - Drops all schemas and the migration log
#   - Runs migrations
#   - Inserts fixtures
#
# For local database:
#   adhoc/dbReset.sh localhost/limber
# For local test database:
#   adhoc/dbReset.sh localhost/limber_test
# For production database:
#   adhoc/dbReset.sh "db-limber-prod-do-user-7022079-0.a.db.ondigitalocean.com:25060/limber?sslmode=require" limber $LIMBER_PROD_POSTGRES_PASSWORD

set -e

./gradlew "-PmainClass=io.limberapp.backend.adhoc.DbResetKt" limber-backend-application:run --args="$*"
