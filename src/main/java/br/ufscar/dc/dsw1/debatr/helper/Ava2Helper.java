package br.ufscar.dc.dsw1.debatr.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Ava2Helper {
    /**
     * @param academicRecord    O RA do aluno.
     * @param plaintextPassword A senha do aluno, em texto puro. Claro, estamos supondo uma conexão via HTTPS.
     * @return [true], se o AVA2 confirmar as credenciais. [false], caso contrário.
     */
    public static boolean verifyUserCredentials(String academicRecord, String plaintextPassword) {

        try {
            HttpsURLConnection connection;
            URL url = new URL("https://ava2.ead.ufscar.br/login/token.php?username=" + academicRecord
                    + "&password=" + plaintextPassword + "&service=moodle_mobile_app");

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Envia a requisição
            final int statusCode = connection.getResponseCode();

            // O AVA2 sempre retorna statusCode == 200, mesmo que a senha esteja errada.
            // Precisamos buscar a palavra "error" manualmente.
            if (statusCode == 200) {
                StringBuilder content = new StringBuilder();
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String currentLine;
                while ((currentLine = input.readLine()) != null) {
                    content.append(currentLine);
                }

                input.close();
                connection.disconnect();

                // Verificações redundantes porque não confio na estabilidade da API do Moodle.
                if (content.toString().contains("error"))
                    return false;
                else if (content.toString().contains("token"))
                    return true;
            }

            // Servidor retornou statusCode != 200. Algo anômalo ocorreu.
            else
                return false;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
