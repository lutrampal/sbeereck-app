import React from 'react';
import { View, Text, FlatList, AsyncStorage } from 'react-native';
import EStyleSheet from 'react-native-extended-stylesheet';
import { Container } from '../components/Container';
import {Header} from '../components/Header';
import { BeerItem } from '../components/BeerItem';
import { YellowButton } from '../components/YellowButton';
import { SearchBox } from '../components/SearchBox';
import { Popup } from '../components/Popup';
import { Loading } from '../components/Loading'
import { AddBeer } from '../components/AddBeer';

EStyleSheet.build({
    $mainBackground: '#F9F9F9'
})

export default class Home extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            initialBeers: [],
            partyBeers: (this.props.navigation.state.params.beers != undefined) ? JSON.parse(JSON.stringify(this.props.navigation.state.params.beers)) : null,
            beers: [],

            loading: false,

            appHost: "",
            appToken: "",
        };
    }

    static navigationOptions = {
        disableGestures: true,
        drawerLockMode: 'locked-closed',
    }

    async componentDidMount() {
        this.setState({ loading: true });

        try {
            let host = await AsyncStorage.getItem('@SbeerEck:host');
            const token = await AsyncStorage.getItem('@SbeerEck:token');

            if (host != null) {
                host = host.replace(" ", "");
                this.setState({ appHost: host });
            }
            if (token != null)
                this.setState({ appToken: token });

            await this.checkConnection();

            if (this.state.connected) {
                this.initialBeers();
            }
        } catch (error) {
            alert("Erreur lors du chargement des paramètres.\n" + error);
            this.setState({ loading: false });
        }
    }

    render() {
        return(
            <Container>
                <Header title="Ajouter des bières" leftButtonAction={() => { this.props.navigation.goBack() }} leftButtonIcon="arrow-left" rightButtonAction={() => { this.props.navigation.state.params.onValidatePress(this.state.initialBeers); this.props.navigation.goBack(); }} rightButtonIcon="check" />

                <SearchBox showBox={true}
                searchText={this.state.searchValue}
                editText={(text) => {
                    this.setState(
                        {
                            searchValue: text,
                            beers: this.state.initialBeers.filter((line) => (this.preg(line.name).includes(this.preg(text))))
                        }
                    );
                    }}/>

                <FlatList
                    data={this.state.beers}
                    extraData={this.state}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({ item }) => <BeerItem onPress={(value) => { this.state.initialBeers.find((obj => obj.id == item.id)).category = value; this.setState({ beers: this.state.initialBeers})}} item={item} />}
                />
                <Loading shown={this.state.loading} />
            </Container>
        );
    }

    async initialBeers() {
        await this.checkConnection();

        this.setState({loading: true});

        try {
            let response = await fetch('https://' + this.state.appHost + '/products?type=beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let request = await response.json();

            if(this.state.partyBeers != null) {

                this.state.partyBeers.forEach(myPartyBeer => {
                    let found = request.findIndex(function (aBeer) {
                        return aBeer.id === myPartyBeer.id;
                    });
                    request[found].category = myPartyBeer.category;
                });
            }

            this.setState({ initialBeers: request})
            this.setState({ beers: this.state.initialBeers, loading: false, searchValue: "" });
            console.log(this.state.beers)
        } catch (error) {
            this.setState({ loading: false, searchValue: "" });
            console.log(error);
        }
    }

    async checkConnection() {
        try {
            let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            this.setState({ connected: true });

        } catch (error) {
            this.setState({ connected: false, loading: false });
            this.props.navigation.navigate("Parameters");
        }
    }

    preg(chaine) {
        let tab = {
            "é": "e", "è": "e", "ê": "e", "ë": "e",
            "ç": "c",
            "à": "a", "â": "a", "ä": "a",
            "î": "i", "ï": "i",
            "ù": "u",
            "ô": "o", "ó": "o", "ö": "o",
            " ": "", "'": "", "-": ""
        };

        for (i in tab) {
            chaine = chaine.toLowerCase().replace(new RegExp(i, "gi"), tab[i])
        }
        return chaine;
    }
}
