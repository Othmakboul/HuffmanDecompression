import java.io.*;

public class HuffmanDecompression {

    // Structure de l'arbre de Huffman
    private static class Node {
        public char value;
        public int frequency;
        public Node left, right;

        public Node(char value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }
    }

    public static void main(String[] args) throws IOException {
        // Étape 1+2 : lecture de l'alphabet et des fréquences et la construction
        BufferedReader reader = new BufferedReader(new FileReader("exemple_freq.txt"));
        int alphabetSize = Integer.parseInt(reader.readLine());
        Node root = null;
        for (int i = 0; i < alphabetSize; i++) {
            char symbol = reader.readLine().charAt(0);
            int frequency = Integer.parseInt(reader.readLine());
            root = insert(root, symbol, frequency);
        }
        reader.close();

        // Étape 3 : décodage du texte compressé
        InputStream input = new FileInputStream("exemple_freq_comp.bin");
        OutputStream output = new FileOutputStream("exemple_freq_decompressed.txt");
        int bit;
        Node node = root;
        while ((bit = input.read()) != -1) {
            if (bit == 0) {
                node = node.left;
            } else if (bit == 1) {
                node = node.right;
            }
            if (node.left == null && node.right == null) {
                output.write(node.value);
                node = root;
            }
        }
        input.close();
        output.close();

        // Étape 4 : détermination du taux de compression
        File compressedFile = new File("exemple_freq_comp.bin");
        File originalFile = new File("exemple_freq.txt");
        double compressionRatio = 1.0 - (double) compressedFile.length() / originalFile.length();
        System.out.println("Taux de compression : " + compressionRatio);

        // Étape 5 : détermination du nombre moyen de bits de stockage d'un caractère du texte compressé
        double bitsPerCharacter = (double) compressedFile.length() * 8 / originalFile.length();
        System.out.println("Nombre moyen de bits par caractère : " + bitsPerCharacter);
    }

    // Fonction pour insérer un nœud dans l'arbre de Huffman
    private static Node insert(Node root, char value, int frequency) {
        if (root == null) {
            return new Node(value, frequency);
        } else if (frequency < root.frequency) {
            Node node = new Node(value, frequency);
            node.left = root;
            return node;
        } else {
            root.right = insert(root.right, value, frequency);
            return root;
        }
    }
}
