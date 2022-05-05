CREATE SCHEMA "brum";
ALTER SCHEMA "brum" OWNER TO "brum4";

REVOKE ALL ON DATABASE "brum4" FROM public;
REVOKE ALL ON SCHEMA "brum" FROM public;

GRANT CONNECT ON DATABASE "brum4" TO "brum4";
GRANT USAGE ON SCHEMA "brum" TO "brum4";

ALTER ROLE "brum4" SET search_path TO "brum";
