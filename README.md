# SortedNucleotideCounter
This code counts the mapped nucleotide number in given SAM file according to given GFF file. Note that this code will not check for multimap or order of the files. These should be and can be adjusted with the used alignment software.

This code uses 3 switches and all 3 of them should be used. Switches are as follows:

-s : SAM file location -o : Output location -g : GFF file location

Example usage: java -Xmx25G -cp MappedNucleotideCount.jar mappednucleotidecount.MappedNucleotideCount -s SAMfile.sam -g GFFfile.gff -o output.txt

NOTE: Since both GFF file and SAM file will be held in RAM, -Xmx switch in Java should be used to increase allowed heapspace.
