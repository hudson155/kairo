if [ -z "$1" ]; then
    echo "No database name"
    exit
fi
./gradlew "-PmainClass=io.limberapp.backend.adhoc.DbResetKt" limber-backend-application:run --args="$1"
