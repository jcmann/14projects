<html>
<body>
<h1>Welcome to the DMBook API</h1>

<main>
    <h2>How to use:</h2>
    <p>
        Generally, this application must be run locally. In order to run it, please complete the initial setup instructions
        first and then skip those if you run it a second time later. The instructions assume you're using IntelliJ, and
        have set it up with an appropriate run configuration with TomEE set up on your machine. The local database
        must also be configured to match the localhibernate config file
        The instructions assume a few things:
    </p>

    <ul>
        <li>You are using IntelliJ</li>
        <li>You have Tomcat/TomEE and a MySQL installation set up that matches the dependencies of this project</li>
        <li>You have set up the database using the SQL dump files provided (which contain sample data)</li>
    </ul>

    <section>
        <h3>Initial Setup</h3>
        <ol>
            <li>Clone down the respository to your local machine</li>
            <li>Open the project in IntelliJ</li>
            <li>Reload the Maven project to index dependencies</li>
            <li>Run the Maven "package" command to build the target directory</li>
            <li>
                Add a new TomEE Run configuration. It should have a Tomcat application server, and its JRE should be
                Java 11. It's pre-launch commands should be the Maven goals: clean, install. It should then build
                the un-exploded WAR file.
            </li>
        </ol>
        <p>
            After this, you can run this configuration and access endpoints! It's intended to be paired with the
            separately built DMBook frontend.
        </p>
    </section>

    <section>
        <h3>How the API works: </h3>
        <section>
            <h4>User Endpoint</h4>
            <p>
                The URL structure for this endpoint is generally: <code>api/users/{jwt}/{endpoint}</code>. The jwt
                must be a valid ID Token for a user in the DMBook user pool (created in the frontend). The endpoint
                can either be: <code>characters</code>, <code>encounters</code>, or <code>all</code>.
            </p>
            <p>
                The <code>characters</code> endpoint retrieves all the user's characters, the <code>encounters</code>
                endpoint all their encounters, and <code>all</code> retrieves a full set of user data needed
                for the DMBook frontend: characters, encounters, and monsters. Monsters are not associated with
                a user, so there is no user monsters endpoint. There are PUT, POST, and DELETE endpoints for each of these
                except all.
            </p>
            <p>The characters and encounters endpoints both require an additional <code>resourceID</code> request
                parameter at minimum. <a href="javadoc">Additional requirements can be found in the documentation.</a></p>
        </section>
        <section>
            <h4>User Endpoint</h4>
            <p>
                The URL structure for this endpoint is generally: <code>api/users/{jwt}/{endpoint}</code>. The jwt
                must be a valid ID Token for a user in the DMBook user pool (created in the frontend). The endpoint
                can either be: <code>characters</code>, <code>encounters</code>, or <code>all</code>.
            </p>
            <p>
                The <code>characters</code> endpoint retrieves all the user's characters, the <code>encounters</code>
                endpoint all their encounters, and <code>all</code> retrieves a full set of user data needed
                for the DMBook frontend: characters, encounters, and monsters. Monsters are not associated with
                a user, so there is no user monsters endpoint.
            </p>
        </section>
    </section>

    <section>
        <h3>Example endpoints</h3>
        <p>For demo purposes, you can see data returned at sample endpoints, which are available here: </p>
        <ul>
            <li><a href="api/characters">Characters</a></li>
            <li><a href="api/encounters">Encounters</a></li>
            <li><a href="api/monsters">Monsters</a></li>
        </ul>
    </section>

</main>
</body>
</html>
