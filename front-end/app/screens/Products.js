import React from 'react';
import { FlatList, Keyboard, AsyncStorage, Alert } from 'react-native';
import EStyleSheet from 'react-native-extended-stylesheet';
import { Container } from '../components/Container';
import {Header} from '../components/Header';
import { ProductItem } from '../components/ProductItem';
import { YellowButton } from '../components/YellowButton';
import { SearchBox } from '../components/SearchBox';
import { Loading } from '../components/Loading';
import { Popup } from '../components/Popup';
import { AddProduct } from '../components/AddProduct';
import { EditProduct } from '../components/EditProduct';
import Toast from "react-native-simple-toast"

EStyleSheet.build({
    $mainBackground: '#F9F9F9'
})

export default class Home extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            newProductName: "",
            newProductPrice: "0.00",
            newProductType: "beer",

            appHost: "",
            appToken: "",
            connected: false,

            loading: false,

            editItem: {},
            editProductName: "",
            editProductType: "",
            editProductPrice: "0.00",

            isSearching: false,
            searchValue: "",

            addPopup: false,
            editPopup: false,

            initialProducts: [],
            products: [],
        };
    }

    async componentDidMount(){
        this.setState({loading: true});

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

            if (this.state.connected)
                this.initiateProducts();
        } catch (error) {
            Toast.show("Erreur lors du chargement des paramètres.\n" + error);
            this.setState({ loading: false });
        }
    }

    static navigationOptions = {
        drawerLabel: "Bières et autres",
    }

    render() {
        return(
            <Container>
                <Header title="Bières et autres" leftButtonIcon="menu" leftButtonAction={() => { this.props.navigation.navigate('DrawerOpen'); Keyboard.dismiss() }} rightButtonAction={() => { this.setState({ isSearching: !this.state.isSearching, searchValue: "", products: this.state.initialProducts }) }} rightButtonIcon="magnify" />

                <SearchBox showBox={this.state.isSearching}
                searchText={this.state.searchValue}
                editText={(text) => {
                    this.setState(
                        {
                            searchValue: text,
                            products: this.state.initialProducts.filter((line) => (this.preg(line.name).includes(this.preg(text))))
                        }
                    );
                    }}/>

                <FlatList
                    refreshing={false}
                    onRefresh={this.initiateProducts.bind(this)}
                    data={this.state.products}
                    extraData={this.state}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({ item }) => <ProductItem item={item} onItemClick={(theItem) => { this.setState({ editPopup: true, editItem: theItem, editProductName: theItem.name, editProductPrice: theItem.price, editProductType: theItem.type})}}/>}
                />

                <YellowButton buttonIcon="plus" buttonAction={() => { this.setState({ addPopup: true, newProductName: "", newProductPrice: "0.00", newProductType: "beer" }) }} />

                <Popup shown={this.state.addPopup} toggleState={() => this.setState({ addPopup: !this.state.addPopup })}>
                    <AddProduct
                        onChangeProductName={(text) => { this.setState({ newProductName: text }) }}
                        newProductName={this.state.newProductName}

                        onMorePress={() => { this.setState({ newProductPrice: (parseFloat(this.state.newProductPrice) + 0.01).toFixed(2).toString() }) }}
                        onLessPress={() => { this.setState({ newProductPrice: (parseFloat(this.state.newProductPrice) - 0.01).toFixed(2).toString() }) }}
                        onChangePrice={(text) => {let editedText = this.setPrice(text); this.setState({ newProductPrice: editedText })}}
                        priceValue={this.state.newProductPrice}

                        newProductType={this.state.newProductType}
                        onTypeChange={(itemValue, itemIndex) => this.setState({ newProductType: itemValue })}

                        onValidatePress={() => {
                            this.setState({loading: true});
                            this.addProduct(this.state.newProductName, this.state.newProductPrice, this.state.newProductType);
                            this.setState({ addPopup: false });
                        }}

                    />
                </Popup>

                <Popup shown={this.state.editPopup} toggleState={() => this.setState({ editPopup: !this.state.editPopup })}>
                    <EditProduct
                        item={this.state.editItem}
                        productName={this.state.editProductName}
                        productType={this.state.editProductType}
                        productPrice={this.state.editProductPrice}

                        onChangeProductName={(text) => { this.setState({ editProductName: text }) }}
                        onMorePress={() => { this.setState({ editProductPrice: (parseFloat(this.state.editProductPrice) + 0.01).toFixed(2).toString() }) }}
                        onLessPress={() => { this.setState({ editProductPrice: (parseFloat(this.state.editProductPrice) - 0.01).toFixed(2).toString() }) }}
                        onChangePrice={(text) => this.setState({ editProductPrice: this.setPrice(text) })}
                        onTypeChange={(itemValue) => { this.setState({ editProductType: itemValue }) }}

                        onValidatePress={(item) => {
                            this.setState({ loading: true });

                            this.editProduct(this.state.editItem.id, this.state.editProductName, this.state.editProductPrice, this.state.editProductType);

                            this.setState({ editPopup: false });
                        }}
                        onSuppressPress={(item) => {
                            Alert.alert(
                                'Supprimer ' + item.name,
                                'Êtes-vous sûr de vouloir supprimer ' + item.name + ' ?',
                                [
                                    { text: 'Annuler', onPress: () => { this.setState({ editPopup: false }) }, style: 'cancel' },
                                    { text: 'Supprimer', onPress: () => {
                                            this.setState({ loading: true });
                                            this.deleteProduct(item.id);
                                            this.setState({ editPopup: false })
                                        }
                                    },
                                ],
                                { cancelable: false }
                            )
                        }}

                    />
                </Popup>

                <Loading shown={this.state.loading} />
            </Container>
        );
    }

    async initiateProducts() {
        await this.checkConnection();

        this.setState({loading: true});

        try {
            let response = await fetch('https://' + this.state.appHost + '/products',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let request = await response.json();

            this.setState({ initialProducts: request });

            this.setState({ products: this.state.initialProducts, loading: false, searchValue: "" });
        } catch (error) {
            this.setState({ loading: false, searchValue: "" });
            console.log(error);
        }
    }

    async addProduct(newProductName, newProductPrice, newProductType) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/products',
                    {
                        method: "post",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "name": newProductName,
                            "price": newProductPrice,
                            "type": newProductType,
                        })
                    });
                this.initiateProducts();
                Toast.show("Produit ajouté !");
            }
        } catch (error) {
            console.log(error);
            this.setState({ loading: false });
        }
    }

    async deleteProduct(productId) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/products/' + productId,
                    {
                        method: "delete",
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                this.initiateProducts();
                Toast.show("Produit supprimé !");
            }
        } catch (error) {
            console.log(error);
            this.setState({ loading: false });
        }
    }

    async editProduct(productId, editProductName, editProductPrice, editProductType) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/products/' + productId,
                    {
                        method: "put",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "name": editProductName,
                            "price": editProductPrice,
                            "type": editProductType,
                        })
                    });

                this.initiateProducts();
                Toast.show("Produit mis à jour !");
            }
        } catch (error) {
            console.log(error);
            this.setState({ loading: false });
        }
    }

    setPrice(text) {
        if (text === "" || text === "0")
            return "0";
        else
            return text.toString().replace(",", ".");
    }

    async checkConnection() {
        try {
            let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let request = await response.json();
            if(request.message != undefined)
            {
                this.setState({connected: false, loading: false});
                this.props.navigation.navigate("Parameters");
            }

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
