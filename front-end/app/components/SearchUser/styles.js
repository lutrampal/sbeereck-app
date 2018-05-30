import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    addTitle: {
        textAlign: 'center',
        fontSize: 25,
        fontWeight: 'bold',
        width: '100%'
    },
    textInput: {
        width: '100%',
        height: 30,
        fontSize: 20,
    },
    containerStyle: {
        flex: 1,
        height: 35,
    },
    inputContainerStyle: {
        width: '100%',
        zIndex: 99, 
        height: 35,
        padding: 7,
        paddingLeft: 7,
        backgroundColor: '#eff0f1',
        borderRadius: 7,
    },
    itemText: {
        backgroundColor: '#eff0f1',
        opacity: 1,
        zIndex: 2, 
        paddingTop: 3,
        paddingBottom: 2,
        fontSize: 22,
        margin: 2
    },
});