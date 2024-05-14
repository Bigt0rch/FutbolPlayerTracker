import requests
from bs4 import BeautifulSoup
import json
import pandas as pd
import sys
import csv
import ast
import os

def get_pages_soups(url_base, num_pages):
    urls = [url_base + f"pagina/{i}" for i in range(1, num_pages + 1)]
    soups = []
    for i, url in enumerate(urls):
        print(f"Descargando página {i + 1} de {len(urls)}")
        html = requests.get(url).content
        soup = BeautifulSoup(html, "html.parser")
        soups.append(soup)
    return soups

def get_urls_players(soups):
    links_jugadores = []
    for i, soup in enumerate(soups):
        print (f"Extrayendo links de la página {i + 1} de {len(soups)}")
        soup.find_all("a", class_="link")
        links = soup.find_all("a", class_="link")

        for link in links:
            if "/es-GB/jugador/" in link.get("href"):
                links_jugadores.append("https://www.laliga.com/" + link.get("href"))
    return links_jugadores


def get_stats_player(url):
    html = str(requests.get(url).content)
    soup = BeautifulSoup(html, "html.parser")

    nombre = soup.find("h1").text.strip()
    posicion = html.split('"jobTitle": "')[1].split('"')[0]
    pais = html.split('"homeLocation":"')[1].split('"')[0]
    altura = html.split('"height":"')[1].split('"')[0]
    peso = html.split('"weight":"')[1].split('"')[0]
    equipo = html.split('"team":{"id":')[1].split('"nickname":"')[1].split('"')[0]
    estadisticas_json = html.split('"playerStats":')[1].split(']')[0] + "]"
    estadisticas = json.loads(estadisticas_json)
    estadisticas_dict = {}
    for stat in estadisticas:
        estadisticas_dict[stat["name"]] = stat["stat"]

    player_dict = {
        "nombre": nombre,
        "url": url,
        "posicion": posicion,
        "pais": pais,
        "altura": altura,
        "peso": peso,
        "equipo": equipo,
    }

    for key, value in estadisticas_dict.items():
        player_dict[key] = value

    return player_dict


def save_to_csv(df, nombre = "player_info.csv"):
    df.to_csv(nombre, index=False, encoding='utf-8')

def decode_csv(input_filename,tmp_filename = "tmp.csv", output_filename = None):

    def decode_value(value):
        return ast.literal_eval('b"' + value + '"').decode('utf-8')

    # Nombre del archivo CSV de entrada y salida
    output_filename = input_filename if output_filename is None else output_filename

    # Leer el archivo CSV de entrada, decodificar los valores y escribir en el archivo temporal
    with open(input_filename, 'r', encoding='utf-8') as input_file, \
        open(tmp_filename, 'w', newline='', encoding='utf-8') as tmp_file:
        reader = csv.reader(input_file)
        writer = csv.writer(tmp_file)
        for row in reader:
            decoded_row = [decode_value(value) for value in row]
            writer.writerow(decoded_row)

    # Copiar el contenido del archivo temporal al archivo de salida
    os.replace(tmp_filename, output_filename)



if __name__ == "__main__":

    # Paso 0: Comprobar argumentos
    if len(sys.argv) < 2 or len(sys.argv) > 3:
        print("Uso: python pyhton_tools/scrapLaLiga.py <output_name> (<num_jugadores>)")
        sys.exit(1)

    # Paso 1: Obtener las páginas de los jugadores
    url_base = "https://www.laliga.com/es-GB/estadisticas/laliga-easports/goleadores/"
    num_pages = 38
    soups = get_pages_soups(url_base, num_pages)

    # Paso 2: Obtener los links de los jugadores
    links_jugadores = get_urls_players(soups)


    # Paso 3: Extraer la información de cada jugador
    players_info = []
    if len(sys.argv) == 3:
        for i, url_player in enumerate(links_jugadores[:int(sys.argv[2])]):
            print(f"Extrayendo información del jugador {i + 1} de {sys.argv[2]}")
            try:
                player_dict = get_stats_player(url_player)
                players_info.append(player_dict)
            except:
                print(f"Error al extraer la información f{url_player}")
    else:
        for i, url_player in enumerate(links_jugadores):
            try:
                print(f"Extrayendo información del jugador {i + 1} de {len(links_jugadores)}")
                player_dict = get_stats_player(url_player)
                players_info.append(player_dict)
            except:
                print(f"Error al extraer la información f{url_player}")


    # Paso 4: Crear DataFrame y guardar en CSV
    df = pd.DataFrame(players_info)
    file_name = sys.argv[1] if ".csv" in sys.argv[1] else sys.argv[1] + ".csv"
    save_to_csv(df, file_name)

    # Paso 5: Decodificar el archivo CSV
    decode_csv(file_name)
    print(f"Se ha guardado la información en {file_name}")