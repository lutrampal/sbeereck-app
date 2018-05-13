# Renouveler le certificat SSL for free

*Idée générale : On va lancer un container Apache accessible sur le port 80 et y monter un volume qui contiendra les fichiers de vérification.*

1. Aller à l'adresse
 https://www.sslforfree.com/create?domains=sbeereck.tk%20www.sbeereck.tk

2. Selectionner "Manual Verification" puis "Manually verify domain name"

3. Télécharger les deux fichiers indiqués

4. Copier ces fichiers sur le serveur :  
`scp -i <clef PEM>  <fichier 1> <fichier 2> ec2-user@sbeereck.tk:~`

5. Se connecter en SSH au server

6. Avant toute chose, faire une sauvegarde de la DB :  
`cp -R mysql-data mysql-data.bak`

7. Créer un dossier pour les fichiers de vérification, il servira de volume hôte pour le container Apache :   
`mkdir verif-files && mv <fichier 1> <fichier 2> verif-files`

8. Lancer le container :  
`sudo docker run -d -p 80:80 -v /home/ec2-user/verif-files:/usr/local/apache2/htdocs/.well-known/acme-challenge --name apache httpd`

    > Les liens demandés par sslforfree doivent désormais être accessibles.

9. Cliquer sur "Download SSL certificate" sur sslforfree

10. S'inscrire à la notification d'expiration

11. Télécharger les fichiers au format zip en local (en cas de pépin avec le serveur)

12. Sur le serveur, supprimer le nouveau container apache :  
`sudo docker rm -f apache`

13. Stopper le webservice :  
`sudo docker stop sbeereck-webservice`

14. Dans le dossier /home/ec2-user/ssl, mettre à jour les fichiers :  
`echo "<certificat>" > /home/ec2-user/ssl/sbeereck.cert`  
`echo "<private key>" > /home/ec2-user/ssl/sbeereck.key`  
`echo "<CA Bundle>" /home/ec2-user/ssl/ca_bundle.cert`

15. Relancer le webservice :  
`sudo docker start sbeereck-webservice`

**C'est fini !  
( •_•)  
( •_•)>⌐■-■  
(⌐■_■)**
