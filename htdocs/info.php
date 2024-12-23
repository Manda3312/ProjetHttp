<?php
// Informations système
echo "<h1>Informations Serveur</h1>";
echo "<table border='1'>";
echo "<tr><th>Propriété</th><th>Valeur</th></tr>";
echo "<tr><td>PHP Version</td><td>" . phpversion() . "</td></tr>";
echo "<tr><td>Système d'exploitation</td><td>" . php_uname() . "</td></tr>";
echo "<tr><td>Date et Heure</td><td>" . date("Y-m-d H:i:s") . "</td></tr>";
echo "<tr><td>Nom du serveur</td><td>" . gethostname() . "</td></tr>";
echo "</table>";

?>