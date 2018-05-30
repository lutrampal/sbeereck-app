import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    container: {
        width: '100%',
        height: 'auto',
        padding: 10,
    },
    textContainer: {
        backgroundColor: '#eff0f1',
        borderRadius: 7,
        height: 35,

    },
    textInput: {
        padding: 7,
        marginTop: 2,
        marginLeft: 30,
        borderRadius: 7,
    },
    leftIconStyle: {
        position: 'absolute',
        zIndex: 1,
        marginTop: 7,
        marginLeft: 10,
    }
});