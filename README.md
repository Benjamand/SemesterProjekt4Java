For at køre programmet:

Jeg antager at i bruger IntelliJ i denne guide. 

Først gå ind i en fil i root directoriet (Den øverste mappe i projektet). Kunne f.eks være pom.xml filen.

Tryk ctrl 2 gange for at indtaste kommandoer.

Brug først: "mvn clean install" for at skabe JAR filerne

Brug derefter denne kommando for at køre programmet: "java --module-path mods-mvn --class-path "libs /*" --module=Core/group3.component.Navigation"



Kig på lektioner om module info og eksempelprojekter i Komponentbaseret for at forstå hvordan man forbinder det hele sammen

Lav jeres interfaces først for at etablere kontrakterne så andre kan bruge dem. Husk at kommunikere med hinanden under processen.

Husk også at bruge TDD undervejs. Lav tests før i laver andet kode.
